package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

public class VirtualMachine extends Machine {

    public VirtualMachine(String id, String operatingSystem, String machineSpec, String mac, String ip, String gateway, String dns, String sshUser, String credentials) {
        super(id, operatingSystem, machineSpec, mac, ip, gateway,dns, sshUser, credentials);
    }
}
