package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public class HashedPasswordAuthentication extends Authentication {
    public HashedPasswordAuthentication(String id, String password) {
        super(id);
        this.password = password;
    }

    public final String getPassword() {
        return password;
    }

    private final String password;
}
