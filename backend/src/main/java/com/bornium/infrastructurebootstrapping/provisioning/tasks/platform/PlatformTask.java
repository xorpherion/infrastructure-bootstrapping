package com.bornium.infrastructurebootstrapping.provisioning.tasks.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Infrastructure;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;
import com.bornium.infrastructurebootstrapping.provisioning.services.CredentialsService;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformTask {

    private CredentialsService credentialsService;
    final Platform platform;
    final List<Infrastructure> infrastructure;

    public PlatformTask(CredentialsService credentialsService, Platform platform, List<Infrastructure> infrastructure) {
        this.credentialsService = credentialsService;
        this.platform = platform;
        this.infrastructure = infrastructure;
    }

    public PlatformTask install() throws Exception {
        platform.install(credentialsService, infrastructure.stream().flatMap(i -> i.allMachines().stream()).collect(Collectors.toList()));
        return this;
    }
}
