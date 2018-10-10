package com.bornium.infrastructurebootstrapping.entities.machine;


public class Disk {

    public enum Type {
        SLOW,
        FAST,
        NETWORK
    }

    Type type;
    Memory size;


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Memory getSize() {
        return size;
    }

    public void setSize(Memory size) {
        this.size = size;
    }
}
