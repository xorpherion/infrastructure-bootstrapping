package com.bornium.infrastructurebootstrapping.provisioning.entities.user;

import java.util.List;

public class AuthorizedKeys {

    String user;
    List<String> authenticationNames;
    List<String> groups;

    public AuthorizedKeys(String user, List<String> authenticationNames, List<String> groups) {
        this.user = user;
        this.authenticationNames = authenticationNames;
        this.groups = groups;
    }

    public String getUser() {
        return user;
    }

    public List<String> getAuthenticationNames() {
        return authenticationNames;
    }

    public List<String> getGroups() {
        return groups;
    }
}
