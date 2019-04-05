package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Virsh;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

@Component
public class HypervisorDeserializer extends GenericTypeFieldDeserializer<Hypervisor> {

    public HypervisorDeserializer(){
        super("type",ImmutableMap.<String,Class>builder()
                .put("virsh", Virsh.class)
                .build()
        );
    }

}
