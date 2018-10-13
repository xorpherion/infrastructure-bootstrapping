package com.bornium.infrastructurebootstrapping.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.entities.user.User;
import com.bornium.infrastructurebootstrapping.processors.hypervisor.HypervisorProcessor;
import com.bornium.infrastructurebootstrapping.processors.hypervisor.VirshProcessor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;

@Entity
public class Virsh extends Hypervisor {

    public Virsh() {
        this(null, null, null, null);
    }

    public Virsh(String host, Integer port, User user, String id) {
        super(host, port, user, id, "hypervisor/virsh");
    }

    @JsonIgnore
    @Override
    public HypervisorProcessor getProcessor() {
        return new VirshProcessor(this);
    }


}
