package com.bornium.boostrappingascode.entities.user

class User {

    String name;
    Authentication authentication;

    User(String name, Authentication authentication) {
        this.name = name
        this.authentication = authentication
    }
}
