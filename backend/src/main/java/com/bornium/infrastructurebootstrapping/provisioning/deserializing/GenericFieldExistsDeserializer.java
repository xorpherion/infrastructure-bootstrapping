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

public class GenericFieldExistsDeserializer<T> extends StdDeserializer<T> {

    final ObjectMapper mapper = createMapper();
    private Map<String, Class> fieldNameToClass;

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParanamerModule());
        return mapper;
    }


    protected GenericFieldExistsDeserializer(Map<String,Class> fieldNameToClass) {
        super(ReflectionUtil.getGenericTypeName(GenericFieldExistsDeserializer.class));
        this.fieldNameToClass = fieldNameToClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map objRaw = p.readValueAs(Map.class);
        for (String fieldName : fieldNameToClass.keySet()) {
            if(objRaw.containsKey(fieldName))
                return (T) mapper.convertValue(objRaw,fieldNameToClass.get(fieldName));
        }
        throw new RuntimeException("Unknown field");
    }
}
