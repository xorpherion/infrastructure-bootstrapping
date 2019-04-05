package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

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
    public String gitVncCommandForInstallAndShutdown(VirtualMachine vm, String helperInstallDevice) {
        return "sudo mount " + helperInstallDevice + " /mnt;sudo coreos-install -d /dev/sda -i /mnt/ignition;sudo shutdown now";
    }

    @Override
    public void createInstallHelperFiles(VirtualMachine vm) throws Exception {
        /*processor.getSsh().execSudoPrint("touch " + processor.vmPath(vm) + "/helper/ignition");
        processor.getSsh().execSudoPrint("bash -c 'echo \"" + createIgnitionFile(vm) + "\" > " + processor.vmPath(vm) + "/helper/ignition'");
        processor.getSsh().execSudoPrint("cp ib/images/" + getImageName() + " " + processor.vmPath(vm) + "/helper/install");*/
    }

    private String createIgnitionFile(VirtualMachine vm) throws IOException {
        return StreamUtils.copyToString(this.getClass().getResourceAsStream("/operatingsystem/containerlinux/ignition-template"), Charset.defaultCharset())
                .replace("\"", "\\\"");
    }
}
