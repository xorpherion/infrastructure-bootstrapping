package com.bornium.infrastructurebootstrapping.deployment.entities;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.BaseId;

import java.util.List;

public class Release extends Base {

    List<Module> modules;

    public Release(String id, List<Module> modules) {
        super(id);
        this.modules = modules;
    }

    public List<Module> getModules() {
        return modules;
    }
}
