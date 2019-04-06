package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class ContainerLinux extends OperatingSystem {

    public ContainerLinux() {
        this("containerlinux","coreos_production_iso_image.iso", "https://stable.release.core-os.net/amd64-usr/current/coreos_production_iso_image.iso");
    }

    @JsonCreator
    public ContainerLinux(String id, String imageName, String downloadLink) {
        super(id, imageName, downloadLink);
    }

    @Override
    public String getVncCommandForInstallAndShutdown(ProvisioningTask task, String helperInstallDevice) {
        return "sudo mount " + helperInstallDevice + " /mnt;sudo coreos-install -d /dev/sda -i /mnt/ignition;sudo shutdown now";
    }

    @Override
    public void createInstallHelperFiles(ProvisioningTask task) throws Exception {
        task.getSsh().execSudoPrint("touch " + Ssh.quote(task.vmPath() + "/helper/ignition"));
        task.getSsh().execSudoPrint("bash -c 'echo \"" + createIgnitionFile(task.getVirtualMachine()) + "\" > " + Ssh.dquote(task.vmPath() + "/helper/ignition")+"'");
        task.getSsh().execSudoPrint("cp " + Ssh.quote("ib/images/" + getImageName()) + " " + Ssh.quote(task.vmPath() + "/helper/install"));
    }

    private String createIgnitionFile(VirtualMachine vm) throws IOException {
        return StreamUtils.copyToString(this.getClass().getResourceAsStream("/operatingsystem/containerlinux/ignition-template"), Charset.defaultCharset())
                .replace("\"", "\\\"");
    }
}
