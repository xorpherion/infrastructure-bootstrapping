package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;

public class User {
    public User(String name, Authentication authentication) {
        this.name = name;
        this.authentication = authentication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    private String name;
    private Authentication authentication;
}
