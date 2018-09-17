package com.bornium.boostrappingascode.repository

import com.bornium.boostrappingascode.entities.Namespace
import org.springframework.data.jpa.repository.JpaRepository

interface NamespaceRepository extends JpaRepository<Namespace,Long> {

}