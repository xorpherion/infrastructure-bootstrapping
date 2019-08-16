package com.bornium.infrastructurebootstrapping.deployment.services;

import com.bornium.infrastructurebootstrapping.Config;
import com.bornium.infrastructurebootstrapping.deployment.tasks.KubernetesReleaseTask;
import org.springframework.stereotype.Service;

@Service
public class ReleaseService {

    public void generate(Config config){
        new KubernetesReleaseTask(config.getReleases()).create();
    }
}
