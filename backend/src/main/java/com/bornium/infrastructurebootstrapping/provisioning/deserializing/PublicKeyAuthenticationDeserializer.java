package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.PlaintextPasswordCredentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.Authentication;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.PublicKeyAuthentication;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PublicKeyAuthenticationDeserializer extends GenericFieldExistsDeserializer<Authentication> {
    protected PublicKeyAuthenticationDeserializer() {
        super(ImmutableMap.<String,Class>builder()
                .put("publicKey", PublicKeyAuthentication.class)
                .build()
        );
    }
}
