package com.bornium.infrastructurebootstrapping.entities.machine;

import com.bornium.infrastructurebootstrapping.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

@Entity
public class VirtualMachine extends Machine {

    public VirtualMachine() {
    }

    public VirtualMachine(String id, OperatingSystem operatingSystem, Disk disk, Memory ram, int cpus, String mac, String ip) {
        super(id, "virtual", operatingSystem, disk, ram, cpus, mac, ip);
    }

    private boolean managed = true;

    public boolean getManaged() {
        return managed;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }
}
