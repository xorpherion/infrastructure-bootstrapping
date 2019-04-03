package com.bornium.infrastructurebootstrapping.provisioning.entities;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MappedSuperclass
public abstract class Base {

    @JsonIgnore
    @Id
    private BaseId baseId;

    public Base(String id){
        this(new BaseId(id,Namespace.GLOBAL));
    }

    public Base(BaseId baseId) {
        this.baseId = baseId;
    }

    @JsonGetter
    public String getId() {
        return baseId.getId();
    }
}
