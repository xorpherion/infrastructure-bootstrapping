package com.bornium.infrastructurebootstrapping.deployment.entities.module;

import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Disk;

public class Mount {

    public enum Type{
        LOCAL,
        NETWORK
    }

    Type type;
    String host;
    String container;

    public Mount( Type type, String host, String container) {
        this.type = type;
        this.host = host;
        this.container = container;
    }

    public Type getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public String getContainer() {
        return container;
    }
}
