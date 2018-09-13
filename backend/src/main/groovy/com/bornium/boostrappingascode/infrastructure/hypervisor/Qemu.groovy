package com.bornium.boostrappingascode.infrastructure.hypervisor

import com.bornium.boostrappingascode.infrastructure.machine.VirtualMachine

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
