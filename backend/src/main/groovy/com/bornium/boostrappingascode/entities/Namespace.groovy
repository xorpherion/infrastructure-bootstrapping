package com.bornium.boostrappingascode.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import java.sql.Timestamp
import java.time.ZonedDateTime

@Entity
class Namespace extends Base {
}
