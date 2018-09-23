package com.bornium.boostrappingascode.commands;

import com.bornium.boostrappingascode.entities.cloud.Cloud;
import com.bornium.boostrappingascode.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CreateCloud {

    final ObjectMapper objectMapper;

    public CreateCloud(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Cloud createCloud(String filename) throws IOException {
        return createCloud(
                objectMapper.readValue(
                        FileUtil.readFileIntoString(filename),
                        Cloud.class));
    }

    public Cloud createCloud(Cloud cloud) {
        cloud.getHypervisors().values().stream().forEach(hypervisor -> hypervisor.createVms());

        return cloud;
    }
}
