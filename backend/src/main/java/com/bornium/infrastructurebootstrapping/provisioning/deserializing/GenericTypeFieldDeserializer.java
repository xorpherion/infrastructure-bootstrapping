package com.bornium.infrastructurebootstrapping.provisioning.deserializing;

import com.bornium.infrastructurebootstrapping.base.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public class GenericTypeFieldDeserializer<T> extends StdDeserializer<T> {
    private String nameField;
    private Map<String, Class> nameToClass;
    private Function<T, String> nameGetter;
    final ObjectMapper mapper = createMapper();

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParanamerModule());
        return mapper;
    }

    protected GenericTypeFieldDeserializer(Map<String,Class> nameToClass) {
        this("id",nameToClass);
    }

    protected GenericTypeFieldDeserializer(String nameField, Map<String,Class> nameToClass) {
        super(ReflectionUtil.getGenericTypeName(GenericTypeFieldDeserializer.class));
        this.nameField = nameField;
        this.nameToClass = nameToClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map objRaw = p.readValueAs(Map.class);
        return (T) mapper.convertValue(objRaw,nameToClass.get(objRaw.get(nameField).toString()));
    }
}
