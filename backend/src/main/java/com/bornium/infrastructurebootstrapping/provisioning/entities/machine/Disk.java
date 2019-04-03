package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;


public class Disk {

    public enum Type {
        HDD,
        SSD,
        NETWORK
    }

    Type type;
    Memory size;

    public Disk(Type type, Memory size) {
        this.type = type;
        this.size = size;
    }

    public static Disk SSD(Memory size){
        return new Disk(Type.SSD,size);
    }

    public static Disk HDD(Memory size){
        return new Disk(Type.HDD,size);
    }

    public static Disk Network(Memory size){
        return new Disk(Type.NETWORK,size);
    }

    public Type getType() {
        return type;
    }

    public Memory getSize() {
        return size;
    }
}
