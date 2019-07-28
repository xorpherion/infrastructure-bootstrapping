package com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough;

public class DiskPassthrough  {

    public enum Type{
        FILE,
        LUN
    }

    Type type;
    String hostSource;
    String guestTarget;

    public DiskPassthrough(Type type, String hostSource, String guestTarget) {
        this.type = type;
        this.hostSource = hostSource;
        this.guestTarget = guestTarget;
    }

    public Type getType() {
        return type;
    }

    public String getHostSource() {
        return hostSource;
    }

    public String getGuestTarget() {
        return guestTarget;
    }
}
