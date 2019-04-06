package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

public class VirshProvisioningTask extends ProvisioningTask {

    public VirshProvisioningTask(Hypervisor hypervisor, Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec) {
        super(hypervisor, loginCredentials, virtualMachine, operatingSystem, machineSpec);
    }

    @Override
    protected void installVm() {

    }

    @Override
    protected void downloadImage() {

    }

    @Override
    protected void createDisks() {

    }

    @Override
    protected void createVMDirectory() {

    }

    @Override
    protected void delete() {

    }
}
