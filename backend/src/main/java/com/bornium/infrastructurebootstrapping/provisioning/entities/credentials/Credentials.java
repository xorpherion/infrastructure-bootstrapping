package com.bornium.infrastructurebootstrapping.provisioning.entities.credentials;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.BaseId;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Namespace;

import javax.persistence.Entity;

public abstract class Credentials extends Base {

    public Credentials(String id) {
        super(id);
    }

    public abstract void applyTo(Ssh ssh);
}
