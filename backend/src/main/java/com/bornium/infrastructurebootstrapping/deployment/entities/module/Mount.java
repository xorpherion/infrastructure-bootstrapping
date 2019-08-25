package com.bornium.infrastructurebootstrapping.deployment.entities.module;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Memory;

public class Mount extends Base {

    public enum Type{
        LOCAL,
        NETWORK,
        SECRET,
        CONFIG
    }

    Type type;
    String storageName;
    String containerPath;
    Memory storageSize;

    public Mount(String id, Type type, String storageName, String containerPath, Memory storageSize) {
        super(id);
        this.type = type;
        this.storageName = storageName;
        this.containerPath = containerPath;
        this.storageSize = storageSize;
    }

    public Type getType() {
        return type;
    }

    public String getStorageName() {
        return storageName;
    }

    public String getContainerPath() {
        return containerPath;
    }

    public Memory getStorageSize() {
        return storageSize;
    }
}
