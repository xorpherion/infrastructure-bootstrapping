package com.bornium.boostrappingascode.entities

import javax.persistence.Embeddable

@Embeddable
class BaseId implements Serializable {
    String id
    String namespace

    BaseId() {
    }

    BaseId(String id, String namespace) {
        this.id = id
        this.namespace = namespace
    }
}
