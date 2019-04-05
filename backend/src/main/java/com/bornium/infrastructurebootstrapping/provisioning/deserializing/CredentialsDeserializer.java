package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.PlaintextPasswordCredentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Virsh;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

@Component
public class CredentialsDeserializer extends GenericFieldExistsDeserializer<Credentials> {

    public CredentialsDeserializer(){
        super(ImmutableMap.<String,Class>builder()
                .put("password", PlaintextPasswordCredentials.class)
                .build()
        );
    }
}
