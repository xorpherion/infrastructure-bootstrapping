package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.KubernetesKubeadm;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
public class PlatformDeserializer extends GenericFieldExistsDeserializer<Platform> {
    protected PlatformDeserializer() {
        super(ImmutableMap.<String,Class>builder()
                .put("masters", KubernetesKubeadm.class)
                .build()
        );
    }
}
