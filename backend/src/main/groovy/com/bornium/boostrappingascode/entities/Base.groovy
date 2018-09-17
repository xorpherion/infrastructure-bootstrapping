package com.bornium.boostrappingascode.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.swagger.annotations.ApiModelProperty

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import java.sql.Timestamp
import java.time.ZonedDateTime

@MappedSuperclass
class Base {

    @JsonUnwrapped
    @EmbeddedId
    BaseId baseId

    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    Timestamp created

    @ApiModelProperty(hidden = true)
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
