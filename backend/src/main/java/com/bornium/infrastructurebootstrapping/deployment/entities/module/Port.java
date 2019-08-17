package com.bornium.infrastructurebootstrapping.deployment.entities.module;

public class Port {

    String protocol;
    String host;
    String container;
    String name;

    public Port(String protocol, String host, String container, String name) {
        this.protocol = protocol;
        this.host = host;
        this.container = container;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
