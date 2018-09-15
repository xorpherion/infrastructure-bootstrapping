package com.bornium.boostrappingascode.repository

import com.bornium.boostrappingascode.entities.machine.PhysicalMachine
import org.springframework.data.jpa.repository.JpaRepository

interface PhysicalMachineRepository extends JpaRepository<PhysicalMachine,Long> {

}