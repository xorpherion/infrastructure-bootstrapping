package com.bornium.infrastructurebootstrapping.deployment.entities;

import com.bornium.infrastructurebootstrapping.deployment.entities.module.Mount;
import com.bornium.infrastructurebootstrapping.deployment.entities.module.Port;
import com.bornium.infrastructurebootstrapping.provisioning.entities.Base;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.*;

@JsonDeserialize(builder = Module.ModuleBuilder.class)
public class Module extends Base {

    public static final String REPLICATION_ALL_NODES = "replication_all_nodes";

    String image;
    List<Port> ports = new ArrayList<>();
    List<Mount> mounts = new ArrayList<>();
    Map<String, String> environment = new HashMap<>();
    String replication;
    boolean stateful;
    Set<String> capabilities = new HashSet<>();

    private Module(String id) {
        super(id);
    }

    @JsonPOJOBuilder()
    public static class ModuleBuilder {

        Module res;

        public ModuleBuilder() {
            this("");
        }

        @JsonIgnore
        public ModuleBuilder(String id) {
            res = new Module(id);
        }

        @JsonProperty("id")
        public ModuleBuilder id(String baseId) {
            res.baseId.setId(baseId);
            return this;
        }

        @JsonProperty("image")
        public ModuleBuilder image(String image) {
            res.image = image;
            return this;
        }

        @JsonProperty("ports")
        public ModuleBuilder ports(Port... ports) {
            res.ports = Arrays.asList(ports);
            return this;
        }

        @JsonProperty("mounts")
        public ModuleBuilder mounts(Mount... mounts) {
            res.mounts = Arrays.asList(mounts);
            return this;
        }

        @JsonProperty("environment")
        public ModuleBuilder environment(Map environment) {
            res.environment = environment;
            return this;
        }

        @JsonProperty("replication")
        public ModuleBuilder replication(String replication) {
            res.replication = replication;
            return this;
        }

        @JsonProperty("stateful")
        public ModuleBuilder stateful(boolean stateful) {
            res.stateful = stateful;
            return this;
        }

        @JsonProperty("capabilities")
        public ModuleBuilder capabilities(String... capabilities) {
            res.capabilities = new HashSet<>(Arrays.asList(capabilities));
            return this;
        }

        public Module build() {
            return res;
        }
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

    public Set<String> getCapabilities() {
        return capabilities;
    }
}
