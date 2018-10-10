package com.bornium.boostrappingascode.entities.machine;

import com.bornium.boostrappingascode.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

@Entity
public class VirtualMachine extends Machine {

    public VirtualMachine() {
        this(null, null, null, null, -1, null);
    }

    public VirtualMachine(String id, OperatingSystem operatingSystem, Disk disk, Memory ram, int cpus, String mac) {
        super(id, "virtual", operatingSystem, disk, ram, cpus, mac);
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
