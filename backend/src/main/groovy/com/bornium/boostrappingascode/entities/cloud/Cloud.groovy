package com.bornium.boostrappingascode.entities.cloud

import com.bornium.boostrappingascode.entities.Base
import com.bornium.boostrappingascode.entities.machine.Machine

import javax.persistence.ElementCollection
import javax.persistence.Entity

@Entity
class Cloud extends Base {

    @ElementCollection
    Map<String, Machine> machines = new HashMap<>();
}
