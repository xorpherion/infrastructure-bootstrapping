package com.bornium.infrastructurebootstrapping;

import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    List<MachineSpec> machineSpecs;
    List<OperatingSystem> operatingSystems;

    List<Cloud> clouds;

    /*public Config(Map<String,Object> configMap) {
        machineSpecs = ((List<Map>) configMap.get("machineSpecs")).stream().map(tRaw -> Util.mapper().convertValue(tRaw,MachineSpec.class)).collect(Collectors.toList());
    }*/

    public Config(List<MachineSpec> machineSpecs, List<OperatingSystem> operatingSystems, List<Cloud> clouds) {
        this.machineSpecs = machineSpecs;
        this.operatingSystems = operatingSystems;
        this.clouds = clouds;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public List<MachineSpec> getMachineSpecs() {
        return machineSpecs;
    }

    public List<OperatingSystem> getOperatingSystems() {
        return operatingSystems;
    }
}
