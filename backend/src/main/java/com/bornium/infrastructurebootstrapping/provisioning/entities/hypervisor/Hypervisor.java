package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.MachineSpec;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;
import com.bornium.infrastructurebootstrapping.provisioning.services.AuthenticationsService;

import java.util.List;

public abstract class Hypervisor extends Base {

    String host;
    Integer port;
    private final String username;
    String loginCredentials;

    String type;

    private List<VirtualMachine> vms;

    public Hypervisor(String id, String host, Integer port, String username, String loginCredentials,  String type, List<VirtualMachine> vms) {
        super(id);
        this.host = host;
        this.port = port;
        this.username = username;
        this.loginCredentials = loginCredentials;
        this.type = type;
        this.vms = vms;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginCredentials() {
        return loginCredentials;
    }

    public String getType() {
        return type;
    }

    public List<VirtualMachine> getVms() {
        return vms;
    }

    public abstract ProvisioningTask createTask(Credentials loginCredentials, VirtualMachine virtualMachine, OperatingSystem operatingSystem, MachineSpec machineSpec, Credentials vmCredentials, AuthenticationsService authenticationsService);
}
