package com.bornium.infrastructurebootstrapping.entities.user;

import com.bornium.infrastructurebootstrapping.access.Ssh;

public class PublicKeyAuthentication extends Authentication {
    @Override
    public void applyTo(Ssh ssh) {
    }

}
