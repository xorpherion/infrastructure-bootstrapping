package com.bornium.boostrappingascode.entities.machine

import com.bornium.boostrappingascode.entities.Base
import com.bornium.boostrappingascode.entities.operatingsystem.OperatingSystem

import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.MappedSuperclass

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Machine extends Base {

    //OperatingSystem os
}
