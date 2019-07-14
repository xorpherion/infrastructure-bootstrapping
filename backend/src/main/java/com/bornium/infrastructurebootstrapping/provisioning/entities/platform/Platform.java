package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Machine;
import com.bornium.infrastructurebootstrapping.provisioning.services.CredentialsService;

import java.util.List;

public abstract class Platform extends Base {

    public Platform(String id) {
        super(id);
    }

    public abstract void install(CredentialsService credentialsService, List<Machine> machines) throws Exception;
}
