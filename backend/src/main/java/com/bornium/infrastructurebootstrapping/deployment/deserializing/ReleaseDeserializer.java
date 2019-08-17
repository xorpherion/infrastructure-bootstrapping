package com.bornium.infrastructurebootstrapping.deployment.deserializing;

import com.bornium.infrastructurebootstrapping.deployment.entities.KubernetesRelease;
import com.bornium.infrastructurebootstrapping.deployment.entities.Release;
import com.bornium.infrastructurebootstrapping.provisioning.deserializing.GenericFieldExistsDeserializer;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.PrivateKeyCredentials;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

@Component
public class ReleaseDeserializer extends GenericFieldExistsDeserializer<Release> {

    public ReleaseDeserializer(){
        super(ImmutableMap.<String,Class>builder()
                .put("dockerRegistryDomainToSecret", KubernetesRelease.class)
                .build()
        );
    }
}
