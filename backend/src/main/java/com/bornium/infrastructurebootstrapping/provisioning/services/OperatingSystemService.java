package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OperatingSystemService extends ConfigMappingService<OperatingSystem>{

    public OperatingSystemService(@Qualifier("config") Map<String, Object> config, ObjectMapper objectMapper) {
        super(config, objectMapper, "operatingSystems", (os) -> os.getId());
    }
}
