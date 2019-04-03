package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

public class PhysicalMachine extends Machine {

    public PhysicalMachine(String id, String operatingSystem, String mac, String ip){
        super(id,"physical",operatingSystem,null,mac,ip);
    }
}
