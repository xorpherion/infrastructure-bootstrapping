package com.bornium.boostrappingascode.entities

import javax.persistence.Embeddable
import javax.persistence.Id;

@Embeddable
class BaseId implements Serializable {
    String id
    String namespace
}
