package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;

public class PasswordAuthentication extends Authentication {
    public PasswordAuthentication(String password) {
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
