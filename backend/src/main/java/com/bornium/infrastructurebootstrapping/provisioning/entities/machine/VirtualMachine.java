package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.AuthorizedKeys;

import javax.persistence.Entity;
import java.util.List;

public class VirtualMachine extends Machine {

    public VirtualMachine(String id, String operatingSystem, String machineSpec, String mac, String ip, String gateway, String dns, String sshUser, String credentials, List<AuthorizedKeys> authorizedKeys) {
        super(id, operatingSystem, machineSpec, mac, ip, gateway,dns, sshUser, credentials);
        this.authorizedKeys = authorizedKeys;
    }

    List<AuthorizedKeys> authorizedKeys;

    public List<AuthorizedKeys> getAuthorizedKeys() {
        return authorizedKeys;
    }
}
