package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;

public abstract class Machine extends Base {

    private String machineSpec;
    private String ip;
    private final String gateway;
    private final String dns;
    String operatingSystem;
    String mac;
    String sshUser;
    String credentials;
    String host;
    int sshPort = 22;

    public Machine(String id, String operatingSystem, String machineSpec, String mac, String host, String ip, String gateway, String dns, String sshUser, String credentials) {
        super(id);
        this.machineSpec = machineSpec;
        this.operatingSystem = operatingSystem;
        this.mac = mac;
        this.host = host;
        this.ip = ip;
        this.gateway = gateway;
        this.dns = dns;
        this.sshUser = sshUser;
        this.credentials = credentials;
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

    public String getSshUser() {
        return sshUser;
    }

    public String getCredentials() {
        return credentials;
    }

    public String getHost() {
        return host;
    }

    public int getSshPort() {
        return sshPort;
    }
}
