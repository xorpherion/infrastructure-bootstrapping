package com.bornium.boostrappingascode.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BaseId implements Serializable {
    public BaseId() {
    }

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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private String id;
    private String namespace;
}
