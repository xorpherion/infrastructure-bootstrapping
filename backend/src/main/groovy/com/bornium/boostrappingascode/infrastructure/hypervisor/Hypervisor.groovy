package com.bornium.boostrappingascode.infrastructure.hypervisor

import com.bornium.boostrappingascode.infrastructure.machine.VirtualMachine
import com.bornium.boostrappingascode.shell.Ssh

abstract class Hypervisor {
    Ssh ssh

    void connect() {

    }

    abstract create(VirtualMachine vm)

    abstract delete(VirtualMachine vm)

}
