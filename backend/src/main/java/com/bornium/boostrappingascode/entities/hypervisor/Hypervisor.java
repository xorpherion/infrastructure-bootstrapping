package com.bornium.boostrappingascode.entities.hypervisor;

import com.bornium.boostrappingascode.entities.Base;
import com.bornium.boostrappingascode.entities.machine.VirtualMachine;
import com.bornium.boostrappingascode.entities.user.User;
import com.bornium.boostrappingascode.processors.hypervisor.HypervisorProcessor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
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
    User user;

    @Transient
    String type;

    @ElementCollection
    private Map<String, VirtualMachine> machines = new HashMap<String, VirtualMachine>();

    public Hypervisor() {
        this(null, null, null, null, null);
    }

    public Hypervisor(String host, Integer port, User user, String id, String type) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.type = type;
        this.getBaseId().setId(id);
    }

    public abstract HypervisorProcessor getProcessor();

    public Map<String, VirtualMachine> getMachines() {
        return machines;
    }

    public void setMachines(Map<String, VirtualMachine> machines) {
        this.machines = machines;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
