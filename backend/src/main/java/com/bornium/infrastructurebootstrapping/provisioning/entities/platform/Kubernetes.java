package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Kubernetes extends Platform {

    final Set<String> masters;
    final Set<String> slaves;
    public Kubernetes(String id, List<String> masters, List<String> slaves) {
        super(id);
        this.masters = masters.stream().collect(Collectors.toSet());
        this.slaves = slaves.stream().collect(Collectors.toSet());
    }

    public Set<String> getMasters() {
        return masters;
    }

    public Set<String> getSlaves() {
        return slaves;
    }
}
