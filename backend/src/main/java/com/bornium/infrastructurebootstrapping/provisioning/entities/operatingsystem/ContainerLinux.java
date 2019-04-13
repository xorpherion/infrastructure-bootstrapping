package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

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
        task.getSsh().execSudoPrint("touch " + Ssh.quote(task.vmPath() + "/ignition"));
        task.getSsh().execSudoPrint("bash -c 'echo \"" + createIgnitionFile(task) + "\" > " + Ssh.dquote(task.vmPath() + "/ignition")+"'");
        task.getSsh().execSudoPrint("cp " + Ssh.quote("ib/images/" + getInstallImage()) + " " + Ssh.quote(task.vmPath() + "/install.bin.bz2"));
    }

    private String createIgnitionFile(ProvisioningTask task) throws IOException {
        return StreamUtils.copyToString(this.getClass().getResourceAsStream("/operatingsystem/containerlinux/ignition-template"), Charset.defaultCharset())
                .replace("\"", "\\\"")
                .replace("${ip}", task.getVirtualMachine().getIp());
    }
}
