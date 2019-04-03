package com.bornium.infrastructurebootstrapping.provisioning.commands;

import com.bornium.infrastructurebootstrapping.base.util.FileUtil;
import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor.HypervisorProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CloudCommands {

    final ObjectMapper objectMapper;

    public CloudCommands(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Cloud fileToCloud(String filename) throws IOException {
        return objectMapper.readValue(
                FileUtil.readFileIntoString(filename),
                Cloud.class);
    }

    public Cloud create(String filename) throws IOException {
        return create(fileToCloud(filename));
    }

    public Cloud create(Cloud cloud) {
        cloud.getHypervisors().stream().forEach(hypervisor -> HypervisorProcessor.createVMs(hypervisor));
        return cloud;
    }

    public Cloud delete(Cloud cloud) {
        cloud.getHypervisors().stream().forEach(hypervisor -> HypervisorProcessor.deleteVMs(hypervisor));
        return cloud;
    }
}
