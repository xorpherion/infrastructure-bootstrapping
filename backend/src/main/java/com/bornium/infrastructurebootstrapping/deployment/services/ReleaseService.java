package com.bornium.infrastructurebootstrapping.deployment.services;

import com.bornium.infrastructurebootstrapping.Config;
import com.bornium.infrastructurebootstrapping.deployment.entities.KubernetesRelease;
import com.bornium.infrastructurebootstrapping.deployment.tasks.KubernetesReleaseTask;
import org.springframework.stereotype.Service;

@Service
public class ReleaseService {

    public void generate(Config config){
        config.getReleases().stream().parallel().forEach(release -> {
            if(release instanceof KubernetesRelease)
                new KubernetesReleaseTask((KubernetesRelease) release).create();
        });
    }
}
