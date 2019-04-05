package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import java.util.List;

public class Virsh extends Hypervisor {

    public Virsh(String id, String host, Integer port, String username, String loginCredentials, List<VirtualMachine> vms) {
        super(id, host, port, username, loginCredentials, "virsh",vms);
    }

//    @JsonIgnore
//    @Override
//    public HypervisorProcessor getProcessor() {
//        return new VirshProcessor(this);
//    }


}
