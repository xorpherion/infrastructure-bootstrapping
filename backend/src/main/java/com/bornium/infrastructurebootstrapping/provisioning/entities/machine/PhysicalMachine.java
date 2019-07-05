package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;

public class PhysicalMachine extends Machine {

    public PhysicalMachine(String id, String operatingSystem, String mac, String host, String ip, String gateway, String dns, String sshUser, String credentials){
        super(id,operatingSystem,null,mac,host,ip, gateway, dns, sshUser, credentials);
    }
}
