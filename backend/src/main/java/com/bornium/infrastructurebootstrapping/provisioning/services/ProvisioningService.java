package com.bornium.infrastructurebootstrapping.provisioning.services;

import org.springframework.stereotype.Service;

@Service
public class ProvisioningService {

    final MachineSpecService machineSpecService;
    final OperatingSystemService operatingSystemService;

    public ProvisioningService(MachineSpecService machineSpecService, OperatingSystemService operatingSystemService) {
        this.machineSpecService = machineSpecService;
        this.operatingSystemService = operatingSystemService;
    }
}
