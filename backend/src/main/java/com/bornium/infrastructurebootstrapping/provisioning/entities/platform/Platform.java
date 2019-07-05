package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.fasterxml.jackson.annotation.JsonCreator;

public abstract class Platform extends Base {

    public Platform(String id) {
        super(id);
    }
}
