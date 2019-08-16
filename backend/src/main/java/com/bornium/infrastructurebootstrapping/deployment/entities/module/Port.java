package com.bornium.infrastructurebootstrapping.deployment.entities.module;

public class Port {

    String protocol;
    String host;
    String container;

    public Port(String protocol, String host, String container) {
        this.protocol = protocol;
        this.host = host;
        this.container = container;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getContainer() {
        return container;
    }
}
