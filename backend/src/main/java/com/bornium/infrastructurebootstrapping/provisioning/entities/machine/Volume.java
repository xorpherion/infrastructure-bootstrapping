package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;

public class Volume extends Base {

    String hostBaseDirectory;
    String guestDevice;
    Memory size;
    String fsType;
    String fsPath;

    public Volume(String id, String hostBaseDirectory, String guestDevice, Memory size, String fsType, String fsPath) {
        super(id);
        this.hostBaseDirectory = hostBaseDirectory;
        this.guestDevice = guestDevice;
        this.size = size;
        this.fsType = fsType;
        this.fsPath = fsPath;
    }

    public String getHostBaseDirectory() {
        return hostBaseDirectory;
    }

    public String getGuestDevice() {
        return guestDevice;
    }

    public Memory getSize() {
        return size;
    }

    public String getFsType() {
        return fsType;
    }

    public String getFsPath() {
        return fsPath;
    }
}
