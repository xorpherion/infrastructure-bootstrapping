package com.bornium.boostrappingascode.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import java.sql.Timestamp
import java.time.Instant
import java.time.ZonedDateTime

@MappedSuperclass
class Base {

    @Id
    @GeneratedValue
    long id

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    Timestamp created

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    Timestamp modified

    @PrePersist
    void prePersist(){
        def ts = Timestamp.from(ZonedDateTime.now().toInstant())
        created = ts
        modified = ts
    }

    @PreUpdate
    void preUpdate(){
        modified = Timestamp.from(ZonedDateTime.now().toInstant())
    }

}
