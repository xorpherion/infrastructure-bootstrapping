package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.services.CredentialsService;
import com.bornium.infrastructurebootstrapping.provisioning.services.MachineSpecService;
import com.bornium.infrastructurebootstrapping.provisioning.services.OperatingSystemService;

public class ProvisioningTask {

    final MachineSpecService machineSpecService;
    final OperatingSystemService operatingSystemService;
    final CredentialsService credentialsService;

    final Hypervisor hypervisor;
    final VirtualMachine virtualMachine;
    final Ssh ssh;

    public ProvisioningTask(MachineSpecService machineSpecService, OperatingSystemService operatingSystemService, CredentialsService credentialsService, Hypervisor hypervisor, VirtualMachine virtualMachine) {
        this.machineSpecService = machineSpecService;
        this.operatingSystemService = operatingSystemService;
        this.credentialsService = credentialsService;
        this.hypervisor = hypervisor;
        this.virtualMachine = virtualMachine;
        this.ssh = new Ssh(hypervisor.getHost(), hypervisor.getPort(), hypervisor.getUsername(), credentialsService.get(hypervisor.getLoginCredentials()));
    }
}
