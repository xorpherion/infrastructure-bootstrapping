package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.Config;
import com.bornium.infrastructurebootstrapping.provisioning.tasks.platform.PlatformTask;
import org.springframework.stereotype.Service;

@Service
public class PlatformService {

    final CredentialsService credentialsService;

    public PlatformService(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    public void install(Config config){
        config.getPlatforms().forEach(platform -> {
            try {
                new PlatformTask(credentialsService, platform,config.getInfrastructures()).install();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
