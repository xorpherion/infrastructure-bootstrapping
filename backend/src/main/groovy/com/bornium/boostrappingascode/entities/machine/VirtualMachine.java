package com.bornium.boostrappingascode.entities.machine;

import javax.persistence.Entity;

@Entity
public class VirtualMachine extends Machine {
    public boolean getManaged() {
        return managed;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    private boolean managed = true;
}
