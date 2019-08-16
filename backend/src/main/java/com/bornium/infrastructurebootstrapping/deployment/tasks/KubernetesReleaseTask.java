package com.bornium.infrastructurebootstrapping.deployment.tasks;

import com.bornium.infrastructurebootstrapping.deployment.entities.Module;
import com.bornium.infrastructurebootstrapping.deployment.entities.Release;
import com.google.common.collect.ImmutableMap;
import io.kubernetes.client.util.Yaml;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KubernetesReleaseTask {

    public enum ControllerType{
        DAEMONSET("apps/v1","DaemonSet"),
        DEPLOYMENT("apps/v1","Deployment"),
        STATEFULSET("apps/v1","StatefulSet");

        private final String apiVersion;
        private final String kind;

        ControllerType(String apiVersion, String kind) {
            this.apiVersion = apiVersion;
            this.kind = kind;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public String getKind() {
            return kind;
        }
    }

    private List<Release> releases;

    public KubernetesReleaseTask(List<Release> releases) {

        this.releases = releases;
    }

    public void create(){
        releases.stream().forEach(release -> {
            release.getModules().stream().forEach(module -> {
                Arrays.asList(
                        createControllers(module),
                        createServices(module),
                        createPersistentVolumes(module),
                        createPersistentVolumeClaims(module)
                )
                        .stream()
                        .filter(m -> m != null)
                        .flatMap(mArr -> Arrays.asList(mArr).stream())
                        .map(m -> removeNullValuesRecursive(m))
                        .forEach(this::writeDescriptor);
            });
        });
    }

    private Map removeNullValuesRecursive(Map<String,Object> m) {
        return m.entrySet().stream()
                .filter(e -> e.getValue() != Optional.empty())
                .map(e -> {
                    if(e.getValue() instanceof Map)
                        return new HashMap.SimpleEntry(e.getKey(), removeNullValuesRecursive((Map<String, Object>) e.getValue()));

                    return e;
                })
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private Map[] createPersistentVolumeClaims(Module module) {
        return null;
    }

    private Map[] createPersistentVolumes(Module module) {
        return null;
    }

    private Map[] createServices(Module module) {
        return null;
    }

    private Map[] createControllers(Module module) {
        return Arrays.asList(
                ImmutableMap
                        .builder()
                        .put("apiVersion", getControllerType(module).getApiVersion())
                        .put("kind", getControllerType(module).getKind())
                        .put("metadata", metadata(module))
                        .put("spec",spec(module))
                        .build()
        ).stream().toArray(Map[]::new);
    }

    private ControllerType getControllerType(Module module) {
        if(module.isStateful())
            return ControllerType.STATEFULSET;

        if(module.getReplication().equals(Module.REPLICATION_ALL_NODES))
            return ControllerType.DAEMONSET;

        return ControllerType.DEPLOYMENT;
    }

    private Map spec(Module module) {
        return ImmutableMap.builder()
                .put("selector", ImmutableMap.builder()
                        .put("matchLabels",ImmutableMap.builder()
                                .put("app", module.getId())
                                .build())
                        .build())
                .put("servieName",module.getId())
                .put("replicas", replication(module))
                .put("template",ImmutableMap.builder()
                        .put("metadata",ImmutableMap.builder()
                                .put("labels",ImmutableMap.builder()
                                        .put("app",module.getId())
                                        .build())
                                .build())
                        .put("spec",ImmutableMap.builder()
                                .put("containers",containers(module))
                                .build())
                        .build())
                .build();
    }

    private Object replication(Module module) {
        if(module.getReplication().equals(Module.REPLICATION_ALL_NODES))
            return Optional.empty();

        return Integer.valueOf(module.getReplication());
    }

    private List<Map> containers(Module module) {
        return Arrays.asList(ImmutableMap.builder()
                .put("name", module.getId())
                .put("image", module.getImage())
                .put("imagePullPolicy","always")
                .put("ports", ports(module))
                .build());
    }

    private Object ports(Module module) {
        if(module.getPorts().size() == 0)
            return Optional.empty();
        return module.getPorts().stream().map(port -> ImmutableMap.builder()
                .put("containerPort",port.getContainer())
                .build()).collect(Collectors.toList());
    }

    private Map metadata(Module module) {
        return ImmutableMap.builder()
                .put("name",module.getId())
                .build();
    }

    private void writeDescriptor(Map map) {
        System.out.println(Yaml.getSnakeYaml().dump(map));
    }
}
