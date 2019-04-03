package com.bornium.infrastructurebootstrapping.provisioning.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BaseId implements Serializable {

    private String id;
    private String namespace;

    public BaseId(String id, String namespace) {
        this.id = id;
        this.namespace = namespace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getNamespace() {
        return namespace;
    }

    @JsonIgnore
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId baseId = (BaseId) o;
        return Objects.equals(id, baseId.id) &&
                Objects.equals(namespace, baseId.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namespace);
    }
}
