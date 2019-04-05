package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

@Component
public class OperatingSystemDeserializer extends GenericTypeFieldDeserializer<OperatingSystem> {
    protected OperatingSystemDeserializer() {
        super(ImmutableMap.<String,Class>builder()
                .put("containerlinux",ContainerLinux.class)
                .build()
        );
    }
}
