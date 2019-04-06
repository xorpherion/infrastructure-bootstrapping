package com.bornium.infrastructurebootstrapping.provisioning.entities.credentials;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public class PlaintextPasswordCredentials extends Credentials {
    public PlaintextPasswordCredentials(String id, String password) {
        super(id);
        this.password = unwrapEnv(password);
    }

    private String unwrapEnv(String password) {
        if(!password.startsWith("$"))
            return password;

        return System.getenv(password.substring(1));
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
