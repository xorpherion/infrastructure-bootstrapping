package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public class PublicKeyAuthentication extends Authentication {

    private final String publicKey;

    public PublicKeyAuthentication(String id, String publicKey) {
        super(id);
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
