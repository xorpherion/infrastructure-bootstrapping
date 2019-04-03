package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor.HypervisorProcessor;
import com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor.VirshProcessor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;

public class Virsh extends Hypervisor {

    public Virsh() {
        this(null, null,null, null, null);
    }

    public Virsh(String host, Integer port, String username, Credentials loginCredentials, String id) {
        super(host, port, username, loginCredentials, id, "virsh");
    }

    @JsonIgnore
    @Override
    public HypervisorProcessor getProcessor() {
        return new VirshProcessor(this);
    }


}
