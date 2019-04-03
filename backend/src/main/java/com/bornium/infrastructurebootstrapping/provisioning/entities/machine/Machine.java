package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem.OperatingSystem;

import javax.persistence.Entity;

public abstract class Machine extends Base {

    private String machineSpec;
    private String ip;
    String type;
    String operatingSystem;
    String mac;

    public Machine(String id, String type, String operatingSystem, String machineSpec, String mac, String ip) {
        super(id);
        this.machineSpec = machineSpec;
        this.type = type;
        this.operatingSystem = operatingSystem;
        this.mac = mac;
        this.ip = ip;
    }

    public String getMachineSpec() {
        return machineSpec;
    }

    public String getIp() {
        return ip;
    }

    public String getType() {
        return type;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getMac() {
        return mac;
    }
}
