package com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;

public class DiskPassthrough extends Base {

    public enum Type{
        DISK,
        LUN
    }

    Type type;
    String hostSource;
    String guestTarget;
    final String mountPath;

    public DiskPassthrough(String id, Type type, String hostSource, String guestTarget, String mountPath) {
        super(id);
        this.type = type;
        this.hostSource = hostSource;
        this.guestTarget = guestTarget;
        this.mountPath = mountPath;
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

    public String getMountPath() {
        return mountPath;
    }
}
