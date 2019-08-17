package com.bornium.infrastructurebootstrapping.deployment.tasks;

import com.bornium.infrastructurebootstrapping.deployment.entities.KubernetesRelease;
import com.bornium.infrastructurebootstrapping.deployment.entities.Module;
import com.google.common.collect.ImmutableMap;
import io.kubernetes.client.util.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private KubernetesRelease release;

    public KubernetesReleaseTask(KubernetesRelease release) {
        this.release = release;
    }

    public void create(){
        release.getModules().stream().parallel().forEach(module -> {
                        Arrays.asList(
                                createControllers(module),
                                createServices(module),
                                createPersistentVolumes(module),
                                createPersistentVolumeClaims(module)
                        )
                                .stream()
                                .parallel()
                                .filter(m -> m != null)
                                .flatMap(mArr -> Arrays.asList(mArr).stream())
                                .map(m -> removeNullValuesRecursive(m))
                                .forEach(m -> writeDescriptor(module, m));
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
        return IntStream.range(0,getControllerType(module) == ControllerType.STATEFULSET ? Integer.parseInt(module.getReplication()) : 1)
                .mapToObj(i -> ImmutableMap
                        .builder()
                        .put("apiVersion","v1")
                        .put("kind","Service")
                        .put("metadata",ImmutableMap.builder()
                                .put("name",getControllerType(module) == ControllerType.STATEFULSET ? module.getId() + "-" + i : module.getId())
                                .build())
                        .put("spec",ImmutableMap.builder()
                                .put("clusterIP", getControllerType(module) == ControllerType.STATEFULSET ? "None" : Optional.empty())
                                .put("selector",ImmutableMap.builder()
                                        .put(getControllerType(module) == ControllerType.STATEFULSET ? "statefulset.kubernetes.io/pod-name" :"app",getControllerType(module) == ControllerType.STATEFULSET ? module.getId()+"-"+i : module.getId())
                                        .build())
                                .put("ports",portsService(module))
                                .build())
                        .build()).toArray(Map[]::new);
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
                .put("serviceName",getControllerType(module) == ControllerType.STATEFULSET ? module.getId() : Optional.empty())
                .put("replicas", replication(module))
                .put("template",ImmutableMap.builder()
                        .put("metadata",ImmutableMap.builder()
                                .put("labels",ImmutableMap.builder()
                                        .put("app",module.getId())
                                        .build())
                                .build())
                        .put("spec",ImmutableMap.builder()
                                .put("containers",containers(module))
                                .put("imagePullSecrets", imagePullSecrets())
                                .build())
                        .build())
                .build();
    }

    private List<Map<String, String>> imagePullSecrets() {
        return release.getDockerRegistryDomainToSecret().entrySet().stream().map(e -> ImmutableMap.of("name",e.getValue())).collect(Collectors.toList());
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
                .put("imagePullPolicy","Always")
                .put("ports", portsController(module))
                .build());
    }

    private Object portsController(Module module) {
        if(module.getPorts().size() == 0)
            return Optional.empty();
        return module.getPorts().stream().map(port -> ImmutableMap.builder()
                .put("containerPort",Integer.parseInt(port.getContainer()))
                .build()).collect(Collectors.toList());
    }

    private Object portsService(Module module) {
        if(module.getPorts().size() == 0)
            return Optional.empty();
        return module.getPorts().stream().map(port -> ImmutableMap.builder()
                .put("protocol",port.getProtocol().toUpperCase())
                .put("port",Integer.parseInt(port.getHost()))
                .put("targetPort",Integer.parseInt(port.getContainer()))
                .put("name",port.getName())
                .build()).collect(Collectors.toList());
    }

    private Map metadata(Module module) {
        return ImmutableMap.builder()
                .put("name",module.getId())
                .build();
    }

    private void writeDescriptor(Module module, Map map) {
        Path p = Paths.get("tmp/release/");
        try {
            if(Files.notExists(p)) {
                Files.createDirectories(p);
            }

            Path filename = p.resolve(module.getId() + "-" + map.get("kind").toString().toLowerCase() + ".yaml");

            if(Files.exists(filename))
                Files.delete(filename);

            Files.write(filename, Yaml.dump(map).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
