package com.bornium.infrastructurebootstrapping.provisioning.entities.cloud;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.PhysicalMachine;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class Infrastructure extends Base {

    private List<Hypervisor> hypervisors;
    private List<PhysicalMachine> physicalMachines;

    public Infrastructure(String id, List<Hypervisor> hypervisors, List<PhysicalMachine> physicalMachines) {
        super(id);
        this.hypervisors = hypervisors;
        this.physicalMachines = physicalMachines;
    }

    public List<Hypervisor> getHypervisors() {
        return hypervisors;
    }

    public List<PhysicalMachine> getPhysicalMachines() {
        return physicalMachines;
    }
}
