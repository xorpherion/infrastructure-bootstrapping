package com.bornium.infrastructurebootstrapping.provisioning.tasks.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Infrastructure;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;

public abstract class PlatformTask {

    final Platform platform;
    final Infrastructure infrastructure;

    protected PlatformTask(Platform platform, Infrastructure infrastructure) {
        this.platform = platform;
        this.infrastructure = infrastructure;
    }

    public void install(){
        platform.install(infrastructure.allMachines());
    }
}
