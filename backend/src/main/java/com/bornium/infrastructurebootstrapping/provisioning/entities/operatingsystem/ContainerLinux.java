package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.FileSystem;
import com.bornium.infrastructurebootstrapping.provisioning.tasks.infrastructure.ProvisioningTask;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContainerLinux extends OperatingSystem {

    public ContainerLinux() {
        this("containerlinux","coreos_production_image.bin.bz2", "coreos_production_iso_image.iso", "https://stable.release.core-os.net/amd64-usr/current/");
    }

    @JsonCreator
    public ContainerLinux(String id, String imageName, String bootImage, String downloadLink) {
        super(id, imageName, bootImage, downloadLink);
    }

    @Override
    public String getVncCommandForInstallAndShutdown(ProvisioningTask task) {
        return "sudo coreos-install -f /ib/install.bin.bz2 -d /dev/sda -i /ib/ignition;sudo shutdown now";
    }

    @Override
    public void createInstallHelperFiles(ProvisioningTask task) throws Exception {
        task.getHypervisorSsh().execSudoPrint("touch " + Ssh.quote(task.vmPath() + "/ignition"));
        task.getHypervisorSsh().execSudoPrint("bash -c 'echo \"" + createIgnitionFile(task) + "\" > " + Ssh.dquote(task.vmPath() + "/ignition")+"'");
        task.getHypervisorSsh().execSudoPrint("cp " + Ssh.quote("ib/images/" + getInstallImage()) + " " + Ssh.quote(task.vmPath() + "/install.bin.bz2"));
    }

    @Override
    public void installPlatform(ProvisioningTask provisioningTask) {

    }

    private String createIgnitionFile(ProvisioningTask task) throws IOException {
        return StreamUtils.copyToString(this.getClass().getResourceAsStream("/operatingsystem/containerlinux/ignition-template"), Charset.defaultCharset())
                .replace("\"", "\\\"")
                .replace("${ip}", task.getVirtualMachine().getIp())
                .replace("${dns}", task.getVirtualMachine().getDns())
                .replace("${gateway}", task.getVirtualMachine().getGateway())
                .replace("${host}", task.getVirtualMachine().getHost())
                .replace("${mounts}", mounts(task))
                .replace("${users}", users(task));
    }

    private String mounts(ProvisioningTask task) {
        return task.getVirtualMachine().getFileSystems().stream().map(this::fsToIgnition).collect(Collectors.joining(","));
    }

    private String fsToIgnition(FileSystem fileSystem) {
        return "{\n" +
                "        \\\"contents\\\": \\\"[Unit]\\nBefore=local-fs.target\\n[Mount]\\nWhat="+fileSystem.getTarget()+"\\nWhere="+fileSystem.getVmPath()+"\\nType=9p\\nOptions=rw,trans=virtio,version=9p2000.L\\n[Install]\\nWantedBy=local-fs.target\\\",\r\n" +
                "        \\\"enable\\\": true,\r\n" +
                "        \\\"name\\\": \\\""+fileSystem.getVmPath().replaceAll(Pattern.quote("/"),"-").substring(1) + ".mount\\\"\r\n" +
                "        }";
    }

    private String users(ProvisioningTask task) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(task.getVirtualMachine().getAuthorizedKeys().stream().map(a -> {
            Map m = new HashMap();
            m.put("name", a.getUser());
            m.put("sshAuthorizedKeys", a.getAuthenticationNames().stream().map(name -> task.getAuthenticationsService().get(name).getValue()).collect(Collectors.toList()));
            m.put("groups", a.getGroups());
            return m;
        }).collect(Collectors.toList())).replace("\"","\\\"");
    }
}
