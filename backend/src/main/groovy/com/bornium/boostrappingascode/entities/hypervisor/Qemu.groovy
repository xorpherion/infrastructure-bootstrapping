package com.bornium.boostrappingascode.entities.hypervisor

import com.bornium.boostrappingascode.entities.machine.VirtualMachine

class Qemu extends Hypervisor {
    @Override
    def create(VirtualMachine vm) {
        return null
    }

    @Override
    def delete(VirtualMachine vm) {
        return null
    }
}
