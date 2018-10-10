package com.bornium.boostrappingascode.entities.machine;

import com.bornium.boostrappingascode.entities.Base;
import com.bornium.boostrappingascode.entities.operatingsystem.OperatingSystem;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PhysicalMachine.class, name = "physical"),
        @JsonSubTypes.Type(value = VirtualMachine.class, name = "virtual"),
})
public abstract class Machine extends Base {

    @Transient
    String type;

    OperatingSystem operatingSystem;

    Disk disk;

    Memory ram;

    int cpus;
    String mac;

    public Machine() {
        this(null, null, null, null, null, -1, null);
    }

    public Machine(String id, String type, OperatingSystem operatingSystem, Disk disk, Memory ram, int cpus, String mac) {
        getBaseId().setId(id);
        this.type = type;
        this.operatingSystem = operatingSystem;
        this.disk = disk;
        this.ram = ram;
        this.cpus = cpus;
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public Memory getRam() {
        return ram;
    }

    public void setRam(Memory ram) {
        this.ram = ram;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
