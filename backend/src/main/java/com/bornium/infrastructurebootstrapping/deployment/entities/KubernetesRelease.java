package com.bornium.infrastructurebootstrapping.deployment.entities;

import java.util.List;
import java.util.Map;

public class KubernetesRelease extends Release {

    final List<Storage> localStorages;
    private final Map<String, String> dockerRegistryDomainToSecret;

    public KubernetesRelease(String id, Map<String, String> dockerRegistryDomainToSecret, List<Module> modules, List<Storage> localStorages) {
        super(id, modules);
        this.dockerRegistryDomainToSecret = dockerRegistryDomainToSecret;
        this.localStorages = localStorages;
    }

    public Map<String, String> getDockerRegistryDomainToSecret() {
        return dockerRegistryDomainToSecret;
    }

    public List<Storage> getLocalStorages() {
        return localStorages;
    }
}
