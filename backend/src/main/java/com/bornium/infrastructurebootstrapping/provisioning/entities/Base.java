package com.bornium.infrastructurebootstrapping.provisioning.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class Base {
    @JsonUnwrapped
    @EmbeddedId
    private BaseId baseId;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Timestamp created;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Timestamp modified;

    public Base() {
        this.baseId = new BaseId();
        this.baseId.setNamespace(Namespace.GLOBAL);
    }

    public Base(BaseId baseId) {
        this.baseId = baseId;
    }

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
}
