package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Platform extends Base {
    @JsonIgnore
    public static final Platform UNKNOWN = new Platform("UNKNOWN");
    @JsonIgnore
    public static final Platform KUBERNETES_KUBEADM = new Platform("KUBERNETES_KUBEADM");

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public Platform(String id) {
        super(id);
    }
}
