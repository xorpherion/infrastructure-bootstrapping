package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Machine;

import java.util.List;

public abstract class Platform extends Base {

    public Platform(String id) {
        super(id);
    }

    public abstract void install(List<Machine> machines);
}
