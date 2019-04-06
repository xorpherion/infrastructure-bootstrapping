package com.bornium.infrastructurebootstrapping.provisioning.services;

import com.bornium.infrastructurebootstrapping.Config;
import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvisioningService {

    final MachineSpecService machineSpecService;
    final OperatingSystemService operatingSystemService;
    final CredentialsService credentialsService;

    public ProvisioningService(MachineSpecService machineSpecService, OperatingSystemService operatingSystemService, CredentialsService credentialsService) {
        this.machineSpecService = machineSpecService;
        this.operatingSystemService = operatingSystemService;
        this.credentialsService = credentialsService;
    }

    public void recreate(Config config){
        config.getClouds().stream().forEach(cloud -> recreate(cloud));
    }

    public void recreate(Cloud cloud){
        cloudToTasks(cloud).forEach(task -> task.recreateVm());
    }

    private List<ProvisioningTask> cloudToTasks(Cloud cloud) {
        return cloud.getHypervisors().stream().map(hypervisor -> hypervisor.getVms().stream().map(vm -> createTask(hypervisor, vm)).collect(Collectors.toList())).flatMap(o -> o.stream()).collect(Collectors.toList());
    }

    private ProvisioningTask createTask(Hypervisor hypervisor, VirtualMachine vm) {
        return hypervisor.createTask(credentialsService.get(hypervisor.getLoginCredentials()),vm,operatingSystemService.get(vm.getOperatingSystem()),machineSpecService.get(vm.getMachineSpec()));
    }
}
