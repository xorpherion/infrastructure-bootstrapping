package com.bornium.infrastructurebootstrapping.deployment.entities;

import com.bornium.infrastructurebootstrapping.deployment.entities.module.Mount;
import com.bornium.infrastructurebootstrapping.deployment.entities.module.Port;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;

import java.util.List;
import java.util.Map;

public class Module extends Base {

    public static final String REPLICATION_ALL_NODES = "replication_all_nodes";

    String image;
    List<Port> ports;
    List<Mount> mounts;
    final Map<String,String> environment;
    final String replication;
    final boolean stateful;

    public Module(String id, String image, List<Port> ports, List<Mount> mounts, Map environment, String replication, boolean stateful) {
        super(id);
        this.image = image;
        this.ports = ports;
        this.mounts = mounts;
        this.environment = environment;
        this.replication = replication;
        this.stateful = stateful;
    }

    public String getImage() {
        return image;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public List<Mount> getMounts() {
        return mounts;
    }

    public Map<String,String> getEnvironment() {
        return environment;
    }

    public String getReplication() {
        return replication;
    }

    public boolean isStateful() {
        return stateful;
    }
}
