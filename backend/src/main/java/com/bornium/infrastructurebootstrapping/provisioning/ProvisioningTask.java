package com.bornium.infrastructurebootstrapping.provisioning;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor.Hypervisor;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

public abstract class ProvisioningTask {
    final Hypervisor hypervisor;
    final VirtualMachine virtualMachine;
    final Ssh ssh;
    private final Credentials loginCredentials;
    private final OperatingSystem operatingSystem;
    private final MachineSpec machineSpec;

    public ProvisioningTask(Hypervisor hypervisor, Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec) {
        this.loginCredentials = loginCredentials;
        this.operatingSystem = operatingSystem;
        this.machineSpec = machineSpec;
        this.hypervisor = hypervisor;
        this.virtualMachine = virtualMachine;
        this.ssh = new Ssh(hypervisor.getHost(), hypervisor.getPort(), hypervisor.getUsername(), loginCredentials);
    }

    public void createVm(){
        createVMDirectory();
        createDisks();
        downloadImage();
        installVm();
    }

    public void deleteVm(){
        delete();
    }

    public void recreateVm(){
        deleteVm();
        createVm();
    }

    protected abstract void installVm();

    protected abstract void downloadImage();

    protected abstract void createDisks();

    protected abstract void createVMDirectory();

    protected abstract void delete();
}
