package com.bornium.boostrappingascode.entities.hypervisor;

import com.bornium.boostrappingascode.entities.machine.VirtualMachine;

import javax.persistence.Entity;

@Entity
public class Qemu extends Hypervisor {
    @Override
    public Object create(VirtualMachine vm) {
        return null;
    }

    @Override
    public Object delete(VirtualMachine vm) {
        return null;
    }

    @Override
    public void createVms() {
        getMachines().values().stream().forEach(vm -> create((VirtualMachine) vm));
    }

}
