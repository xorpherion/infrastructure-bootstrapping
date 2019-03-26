package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public abstract class Authentication {
    public abstract void applyTo(Ssh ssh);
}
