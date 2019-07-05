package com.bornium.infrastructurebootstrapping.provisioning.entities.platform;

import java.util.List;

public class KubernetesKubeadm extends Kubernetes {
    public KubernetesKubeadm(String id, List<String> masters,List<String> slaves) {
        super(id, masters, slaves);
    }
}
