package com.bornium.infrastructurebootstrapping.base.configuration;

import com.bornium.infrastructurebootstrapping.provisioning.deserializing.GenericDeserializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Configuration
public class BeanConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.ignoredParameterTypes(MetaClass.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper(List<GenericDeserializer> deserializers) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParanamerModule());

        SimpleModule deserializerModule = new SimpleModule("SimpleModule",new Version(1,0,0,null));
        deserializers.stream().forEach(des -> deserializerModule.addDeserializer(des.handledType(),des));
        mapper.registerModule(deserializerModule);

        return mapper;
    }

}
