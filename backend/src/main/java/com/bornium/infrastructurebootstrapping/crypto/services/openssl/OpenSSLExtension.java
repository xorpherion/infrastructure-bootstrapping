package com.bornium.infrastructurebootstrapping.crypto.services.openssl;

public enum OpenSSLExtension {
    CA("v3_ca"),
    CA_INTERMEDIATE("v3_intermediate_ca"),
    SERVER("server_cert"),
    CLIENT("usr_cert");

    private String name;

    OpenSSLExtension(String name) {

        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
