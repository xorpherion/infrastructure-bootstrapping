package com.bornium.boostrappingascode.entities;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@MappedSuperclass
public class Base {
    @PrePersist
    public void prePersist() {
        Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
        created = ts;
        modified = ts;
    }

    @PreUpdate
    public void preUpdate() {
        modified = Timestamp.from(ZonedDateTime.now().toInstant());
    }

    public BaseId getBaseId() {
        return baseId;
    }

    public void setBaseId(BaseId baseId) {
        this.baseId = baseId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @JsonUnwrapped
    @EmbeddedId
    private BaseId baseId;
    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Timestamp created;
    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Timestamp modified;
}
