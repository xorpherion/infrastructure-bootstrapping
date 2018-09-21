package com.bornium.boostrappingascode.repository

import com.bornium.boostrappingascode.entities.BaseId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T> extends JpaRepository<T, Long> {
    Optional<T> findOneByBaseId(BaseId baseId)

    Optional<T> deleteByBaseId(BaseId baseId)
}