package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

public class VirtualMachine extends Machine {

    public VirtualMachine(String id, String operatingSystem, String machineSpec, String mac, String ip) {
        super(id, "virtual", operatingSystem, machineSpec, mac, ip);
    }

    private boolean managed = true;

    public boolean isManaged() {
        return managed;
    }
}
