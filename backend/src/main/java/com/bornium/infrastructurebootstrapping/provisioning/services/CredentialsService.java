package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.PlaintextPasswordCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

//@Service
//public class CredentialsService extends ConfigMappingService<PlaintextPasswordCredentials> {
//
//    public CredentialsService(@Qualifier("config") Map<String, Object> config, ObjectMapper objectMapper) {
//        super(config, objectMapper, "plaintextPassword", pp -> pp.getId().getId());
//    }
//}
