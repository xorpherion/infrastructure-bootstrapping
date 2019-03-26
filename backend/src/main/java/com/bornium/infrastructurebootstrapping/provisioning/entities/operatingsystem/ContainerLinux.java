package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor.HypervisorProcessor;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class ContainerLinux extends OperatingSystem {

    public ContainerLinux() {
        super("containerlinux");
    }

    @Override
    public String getVncCommandForInstallAndShutdown(VirtualMachine vm, String helperInstallDevice) {
        return "sudo mount " + helperInstallDevice + " /mnt;sudo coreos-install -d /dev/sda -i /mnt/ignition;sudo shutdown now";
    }

    @Override
    public void createInstallHelperFiles(HypervisorProcessor processor, VirtualMachine vm) throws Exception {
        processor.getSsh().execSudoPrint("touch " + processor.vmPath(vm) + "/helper/ignition");
        processor.getSsh().execSudoPrint("bash -c 'echo \"" + createIgnitionFile(vm) + "\" > " + processor.vmPath(vm) + "/helper/ignition'");
        processor.getSsh().execSudoPrint("cp ib/images/" + vm.getOperatingSystem().getImageName() + " " + processor.vmPath(vm) + "/helper/install");
    }

    @Override
    public String getImageName() {
        return "coreos_production_iso_image.iso";
    }

    @Override
    public String getDownloadLink() {
        return "https://stable.release.core-os.net/amd64-usr/current/coreos_production_iso_image.iso";
    }

    private String createIgnitionFile(VirtualMachine vm) throws IOException {
        return StreamUtils.copyToString(this.getClass().getResourceAsStream("/operatingsystem/containerlinux/ignition-template"), Charset.defaultCharset())
                .replace("\"", "\\\"");
    }
}
