package com.bornium.infrastructurebootstrapping.provisioning.entities.credentials;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public class PlaintextPasswordCredentials extends Credentials {
    public PlaintextPasswordCredentials(String id, String password) {
        super(id);
        this.password = password;
    }

    @Override
    public void applyTo(Ssh ssh) {
        ssh.getSession().setPassword(password);
    }

    public final String getPassword() {
        return password;
    }

    private final String password;
}
