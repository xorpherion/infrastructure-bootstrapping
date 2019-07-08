package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import com.bornium.infrastructurebootstrapping.provisioning.entities.machine.Machine;

import java.util.List;

public class KubernetesKubeadm extends Kubernetes {
    public KubernetesKubeadm(String id, List<String> masters,List<String> slaves) {
        super(id, masters, slaves);
    }

    @Override
    public void install(List<Machine> machines) {

    }
}
