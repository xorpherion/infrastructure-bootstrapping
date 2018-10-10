package com.bornium.boostrappingascode.entities.hypervisor;

import com.bornium.boostrappingascode.entities.user.User;
import com.bornium.boostrappingascode.processors.hypervisor.HypervisorProcessor;
import com.bornium.boostrappingascode.processors.hypervisor.VirshProcessor;

import javax.persistence.Entity;

@Entity
public class Virsh extends Hypervisor {

    public Virsh() {
        this(null, null, null, null);
    }

    public Virsh(String host, Integer port, User user, String id) {
        super(host, port, user, id, "virsh");
    }

    @Override
    public HypervisorProcessor getProcessor() {
        return new VirshProcessor(this);
    }


}
