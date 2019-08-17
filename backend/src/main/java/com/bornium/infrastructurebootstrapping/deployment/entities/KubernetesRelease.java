package com.bornium.infrastructurebootstrapping.deployment.entities;

import com.bornium.infrastructurebootstrapping.deployment.services.ReleaseService;

import java.util.List;
import java.util.Map;

public class KubernetesRelease extends Release {

    private final Map<String, String> dockerRegistryDomainToSecret;

    public KubernetesRelease(String id, Map<String, String> dockerRegistryDomainToSecret, List<Module> modules) {
        super(id, modules);
        this.dockerRegistryDomainToSecret = dockerRegistryDomainToSecret;
    }

    public Map<String, String> getDockerRegistryDomainToSecret() {
        return dockerRegistryDomainToSecret;
    }
}
