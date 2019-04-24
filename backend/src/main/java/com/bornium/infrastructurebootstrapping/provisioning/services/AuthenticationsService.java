package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.provisioning.entities.user.Authentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class AuthenticationsService  extends ConfigMappingService<Authentication>{

    public AuthenticationsService(@Qualifier("config") Map<String, Object> config, ObjectMapper objectMapper) {
        super(config, objectMapper, "authentications", pp -> pp.getId());
    }
}
