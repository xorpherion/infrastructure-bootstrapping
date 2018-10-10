package com.bornium.infrastructurebootstrapping.entities.user;

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
