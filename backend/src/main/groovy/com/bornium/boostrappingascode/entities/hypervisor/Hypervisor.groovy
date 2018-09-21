package com.bornium.boostrappingascode.entities.hypervisor

import com.bornium.boostrappingascode.access.Ssh
import com.bornium.boostrappingascode.entities.machine.VirtualMachine

abstract class Hypervisor {
    Ssh ssh

    void connect() {

    }

    abstract create(VirtualMachine vm)

    abstract delete(VirtualMachine vm)

}
