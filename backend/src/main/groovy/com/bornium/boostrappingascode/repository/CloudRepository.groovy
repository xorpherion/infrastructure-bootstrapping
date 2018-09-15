package com.bornium.boostrappingascode.repository

import com.bornium.boostrappingascode.entities.cloud.Cloud
import org.springframework.data.jpa.repository.JpaRepository

interface CloudRepository extends JpaRepository<Cloud,Long> {
}
