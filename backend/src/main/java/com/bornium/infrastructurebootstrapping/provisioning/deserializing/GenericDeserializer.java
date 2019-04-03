package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.base.util.ReflectionUtil;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.ContainerLinux;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

import java.io.IOException;
import java.util.Map;

public class GenericDeserializer<T> extends StdDeserializer<T> {
    private Map<String, Class> nameToClass;
    final ObjectMapper mapper = createMapper();

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParanamerModule());
        return mapper;
    }

    protected GenericDeserializer(Map<String,Class> nameToClass) {
        super(ReflectionUtil.getGenericTypeName(GenericDeserializer.class));
        this.nameToClass = nameToClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map objRaw = p.readValueAs(Map.class);
        return (T) mapper.convertValue(objRaw,nameToClass.get(objRaw.get("id").toString()));
    }
}
