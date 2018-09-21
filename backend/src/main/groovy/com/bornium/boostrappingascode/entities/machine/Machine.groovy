package com.bornium.boostrappingascode.entities.machine

import com.bornium.boostrappingascode.entities.Base

import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Machine extends Base {

    //OperatingSystem os
}
