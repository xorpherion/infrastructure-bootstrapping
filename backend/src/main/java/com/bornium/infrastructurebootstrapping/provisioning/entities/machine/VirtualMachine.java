package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.DiskPassthrough;
import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough.PciPassthrough;
import com.bornium.infrastructurebootstrapping.provisioning.entities.platform.Platform;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.AuthorizedKeys;

import java.util.List;

public class VirtualMachine extends Machine {

    public VirtualMachine(String id, String operatingSystem, String machineSpec, String mac, String host, String ip, String gateway, String dns, String sshUser, String credentials, List<AuthorizedKeys> authorizedKeys,List<DiskPassthrough> diskPassthroughs, List<PciPassthrough> pciPassthroughs) {
        super(id, operatingSystem, machineSpec, mac, host, ip, gateway,dns, sshUser, credentials);
        this.authorizedKeys = authorizedKeys;
        this.diskPassthroughs = diskPassthroughs;
        this.pciPassthroughs = pciPassthroughs;
    }

    List<AuthorizedKeys> authorizedKeys;
    List<DiskPassthrough> diskPassthroughs;
    List<PciPassthrough> pciPassthroughs;

    public List<AuthorizedKeys> getAuthorizedKeys() {
        return authorizedKeys;
    }
    public List<DiskPassthrough> getDiskPassthroughs() {
        return diskPassthroughs;
    }
    public List<PciPassthrough> getPciPassthroughs() {
        return pciPassthroughs;
    }
}
