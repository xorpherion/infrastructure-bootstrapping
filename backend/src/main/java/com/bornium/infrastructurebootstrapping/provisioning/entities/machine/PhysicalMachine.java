package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;

public class PhysicalMachine extends Machine {

    public PhysicalMachine(String id, String operatingSystem, String mac, String ip, String gateway, String dns, String sshUser, String credentials){
        super(id,operatingSystem,null,mac,ip, gateway, dns, sshUser, credentials);
    }
}
