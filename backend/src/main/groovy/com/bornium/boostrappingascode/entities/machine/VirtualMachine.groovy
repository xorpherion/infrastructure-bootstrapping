package com.bornium.boostrappingascode.entities.machine

import com.bornium.boostrappingascode.entities.operatingsystem.OperatingSystem

import javax.persistence.Entity

@Entity
class VirtualMachine extends Machine {
    boolean managed = true

}
