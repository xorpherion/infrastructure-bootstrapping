package com.bornium.infrastructurebootstrapping.deployment.entities.module;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Memory;
import com.fasterxml.jackson.annotation.JsonCreator;

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
    String singleFileName;

    public Mount(String id, Type type, String storageName, String containerPath, Memory storageSize) {
        this(id, type, storageName, containerPath, storageSize, null);
    }

    @JsonCreator
    public Mount(String id, Type type, String storageName, String containerPath, Memory storageSize, String singleFileName) {
        super(id);
        this.type = type;
        this.storageName = storageName;
        this.containerPath = containerPath;
        this.storageSize = storageSize;
        this.singleFileName = singleFileName;
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

    public String getSingleFileName() {
        return singleFileName;
    }
}
