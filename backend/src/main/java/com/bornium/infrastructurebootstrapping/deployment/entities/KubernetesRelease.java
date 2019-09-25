package com.bornium.infrastructurebootstrapping.deployment.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class KubernetesRelease extends Release {

    final List<Storage> localStorages;
    private final Map<String, String> dockerRegistryDomainToSecret;
    final HashSet<String> gateway;

    public KubernetesRelease(String id, Map<String, String> dockerRegistryDomainToSecret, List<Module> modules, List<Storage> localStorages, HashSet<String> gateway) {
        super(id, modules);
        this.dockerRegistryDomainToSecret = dockerRegistryDomainToSecret;
        this.localStorages = localStorages;
        this.gateway = gateway;
    }

    public Map<String, String> getDockerRegistryDomainToSecret() {
        return dockerRegistryDomainToSecret;
    }

    public List<Storage> getLocalStorages() {
        return localStorages;
    }

    public HashSet<String> getGateway() {
        return gateway;
    }
}
