package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineSpecService extends ConfigMappingService<MachineSpec>{

    public MachineSpecService(@Qualifier("config") Map<String, Object> config, ObjectMapper objectMapper) {
        super(config, objectMapper, "machineSpecs", (spec) -> spec.getId());
    }
}
