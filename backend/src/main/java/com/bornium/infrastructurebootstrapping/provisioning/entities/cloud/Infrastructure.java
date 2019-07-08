package com.bornium.infrastructurebootstrapping.provisioning.entities.cloud;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Machine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.PhysicalMachine;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Machine> allMachines() {
        return Stream.concat(
                getPhysicalMachines()
                        .stream(),
                        //.map(physicalMachine -> (Machine) physicalMachine),
                getHypervisors()
                        .stream()
                        .map(h -> h.getVms())
                        .flatMap(vms -> vms.stream()))
                        //.map(vm -> (Machine)vm))
                .collect(Collectors.toList());
    }
}
