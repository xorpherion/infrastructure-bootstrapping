package com.bornium.infrastructurebootstrapping.provisioning.entities.hypervisor;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.bornium.infrastructurebootstrapping.provisioning.processors.hypervisor.HypervisorProcessor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        //@JsonSubTypes.Type(value = Qemu.class, name = "qemu"),
        @JsonSubTypes.Type(value = Virsh.class, name = "virsh"),
})
public abstract class Hypervisor extends Base {

    String host;
    Integer port;
    private final String username;


    Credentials loginCredentials;

    @Transient
    String type;

    @ElementCollection
    private List<VirtualMachine> vms = new ArrayList<VirtualMachine>();

    public Hypervisor() {
        this(null, null, null, null, null, null);
    }

    public Hypervisor(String host, Integer port, String username, Credentials loginCredentials, String id, String type) {
        super(id);
        this.host = host;
        this.port = port;
        this.username = username;
        this.loginCredentials = loginCredentials;
        this.type = type;
    }

    public abstract HypervisorProcessor getProcessor();

    public List<VirtualMachine> getVms() {
        return vms;
    }

    public void setVms(List<VirtualMachine> vms) {
        this.vms = vms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Credentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(Credentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public String getUsername() {
        return username;
    }
}
