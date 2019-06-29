package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Authentication extends Base {
    public Authentication(String id) {
        super(id);
    }

    @JsonIgnore
    public abstract String getValue();
}
