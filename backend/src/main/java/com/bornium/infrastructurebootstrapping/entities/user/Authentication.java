package com.bornium.infrastructurebootstrapping.entities.user;

import com.bornium.infrastructurebootstrapping.access.Ssh;

public abstract class Authentication {
    public abstract void applyTo(Ssh ssh);
}
