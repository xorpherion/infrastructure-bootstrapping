package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.bornium.infrastructurebootstrapping.provisioning.entities.BaseId;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.VirtualMachine;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public abstract class OperatingSystem extends Base {

    String imageName;
    String downloadLink;

    public OperatingSystem(String id, String imageName, String downloadLink) {
        super(id);
        this.imageName = imageName;
        this.downloadLink = downloadLink;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public abstract String gitVncCommandForInstallAndShutdown(VirtualMachine vm, String helperInstallDevice);

    public abstract void createInstallHelperFiles(VirtualMachine vm) throws Exception;
}
