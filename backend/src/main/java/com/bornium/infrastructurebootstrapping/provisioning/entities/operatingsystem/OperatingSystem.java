package com.bornium.infrastructurebootstrapping.provisioning.entities.operatingsystem;

import com.bornium.infrastructurebootstrapping.provisioning.ProvisioningTask;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;

public abstract class OperatingSystem extends Base {

    String installImage;
    String bootImage;
    String downloadLink;

    public OperatingSystem(String id, String installImage, String bootImage, String downloadLink) {
        super(id);
        this.installImage = installImage;
        this.bootImage = bootImage;
        this.downloadLink = downloadLink;
    }

    public String getInstallImage() {
        return installImage;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getBootImage() {
        return bootImage;
    }

    public abstract String getVncCommandForInstallAndShutdown(ProvisioningTask task);

    public abstract void createInstallHelperFiles(ProvisioningTask task) throws Exception;

    public abstract void installPlatform(ProvisioningTask provisioningTask);
}
