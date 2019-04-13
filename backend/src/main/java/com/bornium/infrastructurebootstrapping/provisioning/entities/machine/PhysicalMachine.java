package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

public class PhysicalMachine extends Machine {

    public PhysicalMachine(String id, String operatingSystem, String mac, String ip, String gateway, String dns){
        super(id,operatingSystem,null,mac,ip, gateway, dns);
    }
}
