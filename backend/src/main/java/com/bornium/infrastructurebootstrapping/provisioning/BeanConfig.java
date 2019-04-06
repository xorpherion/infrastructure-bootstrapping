package com.bornium.infrastructurebootstrapping.provisioning;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfig {

    @Bean("config")
    public Map<String,Object> config(ObjectMapper objectMapper, @Value("${config:config.json}") String configFile) throws IOException {
        Path configPath = Paths.get(configFile);
        if(!configPath.toFile().exists())
            return new HashMap<>();
        return objectMapper.readValue(Files.readAllBytes(configPath), Map.class);
    }

}
