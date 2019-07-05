package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import java.util.List;

public class Kubernetes extends Platform {

    final List<String> masters;
    final List<String> slaves;
    public Kubernetes(String id, List<String> masters, List<String> slaves) {
        super(id);
        this.masters = masters;
        this.slaves = slaves;
    }

    public List<String> getMasters() {
        return masters;
    }

    public List<String> getSlaves() {
        return slaves;
    }
}
