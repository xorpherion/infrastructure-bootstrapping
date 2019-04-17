package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.VirshProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import java.util.List;

public class Virsh extends Hypervisor {

    public Virsh(String id, String host, Integer port, String username, String loginCredentials, List<VirtualMachine> vms) {
        super(id, host, port, username, loginCredentials, "virsh",vms);
    }

    @Override
    public ProvisioningTask createTask(Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec, Credentials vmCredentials) {
        return new VirshProvisioningTask(this,loginCredentials,virtualMachine,operatingSystem,machineSpec,vmCredentials);
    }
}
