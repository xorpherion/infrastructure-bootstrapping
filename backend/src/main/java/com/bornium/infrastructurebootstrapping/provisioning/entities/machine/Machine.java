package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;

public abstract class Machine extends Base {

    private String machineSpec;
    private String ip;
    private final String gateway;
    private final String dns;
    String operatingSystem;
    String mac;

    public Machine(String id, String operatingSystem, String machineSpec, String mac, String ip, String gateway, String dns) {
        super(id);
        this.machineSpec = machineSpec;
        this.operatingSystem = operatingSystem;
        this.mac = mac;
        this.ip = ip;
        this.gateway = gateway;
        this.dns = dns;
    }

    public String getMachineSpec() {
        return machineSpec;
    }

    public String getIp() {
        return ip;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getMac() {
        return mac;
    }

    public String getGateway() {
        return gateway;
    }

    public String getDns() {
        return dns;
    }
}
