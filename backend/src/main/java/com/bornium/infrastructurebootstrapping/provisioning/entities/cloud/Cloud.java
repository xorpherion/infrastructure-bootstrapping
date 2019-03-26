package com.bornium.infrastructurebootstrapping.provisioning.entities.cloud;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.PhysicalMachine;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Cloud extends Base {

    @ElementCollection
    private Map<String, Hypervisor> hypervisors = new HashMap<>();

    @ElementCollection
    private Map<String, PhysicalMachine> machines = new HashMap<String, PhysicalMachine>();


    public Map<String, Hypervisor> getHypervisors() {
        return hypervisors;
    }

    public void setHypervisors(Map<String, Hypervisor> hypervisors) {
        this.hypervisors = hypervisors;
    }

    public Map<String, PhysicalMachine> getMachines() {
        return machines;
    }

    public void setMachines(Map<String, PhysicalMachine> machines) {
        this.machines = machines;
    }
}
