package com.bornium.infrastructurebootstrapping.base.configuration;

import com.bornium.infrastructurebootstrapping.provisioning.deserializing.GenericFieldExistsDeserializer;
import com.bornium.infrastructurebootstrapping.provisioning.deserializing.GenericTypeFieldDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.stream.Stream;

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
    public ObjectMapper objectMapper(List<GenericTypeFieldDeserializer> deserializers, List<GenericFieldExistsDeserializer> deserializers2) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.registerModule(new ParanamerModule());

        SimpleModule deserializerModule = new SimpleModule("SimpleModule",new Version(1,0,0,null));
        Stream.concat(deserializers.stream(),deserializers2.stream()).forEach(des -> deserializerModule.addDeserializer(des.handledType(),des));
        mapper.registerModule(deserializerModule);

        return mapper;
    }

}
