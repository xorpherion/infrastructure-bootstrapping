package com.bornium.infrastructurebootstrapping.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.entities.machine.VirtualMachine;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContainerLinux.class, name = "containerlinux"),
})
public abstract class OperatingSystem {

    String type;

    public abstract String getImageName();

    public abstract String getDownloadLink();

    public abstract void installOS(VirtualMachine vm);

    public OperatingSystem() {
        this(null);
    }

    public OperatingSystem(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
