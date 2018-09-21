package com.bornium.boostrappingascode.entities.machine


import javax.persistence.Entity

@Entity
class VirtualMachine extends Machine {
    boolean managed = true

}
