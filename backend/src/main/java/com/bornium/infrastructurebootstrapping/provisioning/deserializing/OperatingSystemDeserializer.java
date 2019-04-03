package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OperatingSystemDeserializer extends GenericDeserializer<OperatingSystem> {
    protected OperatingSystemDeserializer() {
        super(ImmutableMap.<String,Class>builder()
                .put("containerlinux",ContainerLinux.class)
                .build()
        );
    }
}
