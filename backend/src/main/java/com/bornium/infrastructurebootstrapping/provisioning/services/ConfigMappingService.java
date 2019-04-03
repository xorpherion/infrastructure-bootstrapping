package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.base.util.ReflectionUtil;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Qualifier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bornium.infrastructurebootstrapping.base.util.ReflectionUtil.getGenericTypeName;
import static java.util.Arrays.*;

public class ConfigMappingService<T> {



    Map<String, T> mapper;

    public ConfigMappingService(Map<String,Object> config, ObjectMapper objectMapper, String name, Function<T,String> toName){
        Object obj = ((Map) config.get("config")).get(name);
        if(obj == null){
            mapper = new HashMap<>();
            System.out.println("WARNING");
            return;
        }
        T[] configAsObject = (T[]) objectMapper.convertValue(obj, Array.newInstance(getGenericTypeName(ConfigMappingService.class), 0).getClass());
        mapper = asList(configAsObject).stream().collect(Collectors.toMap(o -> toName.apply(o), o -> o));
    }

    public T get(String name){
        return mapper.get(name);
    }
}
