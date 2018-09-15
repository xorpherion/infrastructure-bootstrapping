package com.bornium.boostrappingascode.entities.cloud

import com.bornium.boostrappingascode.entities.Base
import com.bornium.boostrappingascode.entities.machine.Machine
import com.bornium.boostrappingascode.entities.machine.PhysicalMachine
import com.bornium.boostrappingascode.entities.machine.VirtualMachine

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Cloud extends Base {

    @ElementCollection
    Map<String, Machine> machines = new HashMap<>();
}
