package com.bornium.infrastructurebootstrapping;

import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.Authentication;

import java.util.List;

public class Config {

    final List<MachineSpec> machineSpecs;
    final List<OperatingSystem> operatingSystems;
    final List<Credentials> credentials;
    final List<Authentication> authentications;
    final List<Cloud> clouds;

    public Config(List<MachineSpec> machineSpecs, List<OperatingSystem> operatingSystems, List<Credentials> credentials, List<Authentication> authentications, List<Cloud> clouds) {
        this.machineSpecs = machineSpecs;
        this.operatingSystems = operatingSystems;
        this.credentials = credentials;
        this.authentications = authentications;
        this.clouds = clouds;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public List<MachineSpec> getMachineSpecs() {
        return machineSpecs;
    }

    public List<OperatingSystem> getOperatingSystems() {
        return operatingSystems;
    }

    public List<Credentials> getCredentials() {
        return credentials;
    }

    public List<Authentication> getAuthentications() {
        return authentications;
    }
}
