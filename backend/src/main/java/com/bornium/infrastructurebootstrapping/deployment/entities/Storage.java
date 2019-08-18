package com.bornium.infrastructurebootstrapping.deployment.entities;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Disk;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Memory;

public class Storage extends Base {

    Disk.Type type;
    Memory size;
    String hostpath;

    public Storage(String id, Disk.Type type, Memory size, String hostpath) {
        super(id);
        this.type = type;
        this.size = size;
        this.hostpath = hostpath;
    }

    public Disk.Type getType() {
        return type;
    }

    public Memory getSize() {
        return size;
    }

    public String getHostpath() {
        return hostpath;
    }
}
