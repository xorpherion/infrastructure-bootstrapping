package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Map;

public class MachineSpec extends Base {

    List<Disk> disks;
    Memory ram;
    Integer cpus;

    public MachineSpec(String id, List<Disk> disks, Memory ram, Integer cpus) {
        super(id);
        this.disks = disks;
        this.ram = ram;
        this.cpus = cpus;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public Memory getRam() {
        return ram;
    }

    public Integer getCpus() {
        return cpus;
    }
}
