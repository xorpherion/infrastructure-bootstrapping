package com.bornium.boostrappingascode.entities.hypervisor

import com.bornium.boostrappingascode.entities.machine.VirtualMachine
import com.bornium.boostrappingascode.access.Ssh

abstract class Hypervisor {
    Ssh ssh

    void connect() {

    }

    abstract create(VirtualMachine vm)

    abstract delete(VirtualMachine vm)

}
