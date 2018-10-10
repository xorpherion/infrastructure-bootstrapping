package com.bornium.boostrappingascode.entities.user;

import com.bornium.boostrappingascode.access.Ssh;

public abstract class Authentication {
    public abstract void applyTo(Ssh ssh);
}
