package com.bornium.boostrappingascode.entities.operatingsystem;

import com.bornium.boostrappingascode.entities.machine.VirtualMachine;

public class ContainerLinux extends OperatingSystem {

    public ContainerLinux() {
        super("containerlinux");
    }

    @Override
    public String getImageName() {
        return "coreos_production_iso_image.iso";
    }

    @Override
    public String getDownloadLink() {
        return "https://stable.release.core-os.net/amd64-usr/current/coreos_production_iso_image.iso";
    }

    @Override
    public void installOS(VirtualMachine vm) {

    }
}
