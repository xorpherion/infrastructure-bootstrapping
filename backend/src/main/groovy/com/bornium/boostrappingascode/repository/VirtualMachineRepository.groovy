package com.bornium.boostrappingascode.repository

import com.bornium.boostrappingascode.entities.machine.PhysicalMachine
import com.bornium.boostrappingascode.entities.machine.VirtualMachine
import org.springframework.data.jpa.repository.JpaRepository

interface VirtualMachineRepository extends JpaRepository<VirtualMachine,Long> {

}