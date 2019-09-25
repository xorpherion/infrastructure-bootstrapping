package com.bornium.infrastructurebootstrapping.deployment.tasks;

import com.bornium.infrastructurebootstrapping.deployment.entities.KubernetesRelease;
import com.bornium.infrastructurebootstrapping.deployment.entities.Module;
import com.bornium.infrastructurebootstrapping.deployment.entities.module.Mount;
import com.google.common.collect.ImmutableMap;
import io.kubernetes.client.util.Yaml;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    private KubernetesRelease release;

    public KubernetesReleaseTask(KubernetesRelease release) {
        this.release = release;
    }

    public void create(){
        writeClusterEssentialsDescriptors();
        writeStorageClassDescriptors();
        writeModuleDescriptors();
    }

    private void writeClusterEssentialsDescriptors() {
        loadEssential("local-provisioner").stream()
                .map(m -> {
                    if(!m.get("kind").toString().toLowerCase().equals("daemonset"))
                        return m;

                    ((Map)((List)((Map)((Map)((Map)m.get("spec")).get("template")).get("spec")).get("containers")).get(0)).put("volumeMounts",ldpVolumeMounts());
                    ((Map)((Map)((Map)m.get("spec")).get("template")).get("spec")).put("volumes",ldpVolumes());
                    return m;
                })
                .forEach(m -> writeDescriptor("local-provisioner",m));
    }

    private Object ldpVolumes() {
        return release.getLocalStorages().stream().map(ls -> ImmutableMap.builder()
                .put("hostPath", ImmutableMap.builder()
                        .put("path",ls.getHostpath())
                        .put("type","")
                        .build())
                .put("name",ls.getId())
                .build()).collect(Collectors.toList());
    }

    private Object ldpVolumeMounts() {
        return release.getLocalStorages().stream().map(ls -> ImmutableMap.builder()
                .put("mountPath",ls.getHostpath())
                .put("mountPropagation","HostToContainer")
                .put("name",ls.getId())
                .build()).collect(Collectors.toList());
    }

    private List<Map> loadEssential(String folderName) {
        Path fixedFolderName = Paths.get("kubernetes/" + folderName);
        try {
            return findAllFilesInJarFolder(fixedFolderName).filter(p -> !p.toFile().isDirectory()).map(p -> {
                try {
                        return Yaml.loadAs(new String(Files.readAllBytes(p)),Map.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void writeEssential(String folderName) {
        Path fixedFolderName = Paths.get("kubernetes/" + folderName);

        try {
            findAllFilesInJarFolder(fixedFolderName).filter(p -> !p.toFile().isDirectory()).forEach(p -> {
                try {
                    writeDescriptor(folderName,Yaml.loadAs(new String(Files.readAllBytes(p)),Map.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stream<Path> findAllFilesInJarFolder(Path subPath) throws URISyntaxException, IOException {
        URI uri = this.getClass().getClassLoader().getResource(subPath.toString()).toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/resources");
        } else {
            myPath = Paths.get(uri);
        }
        return Files.walk(myPath, Integer.MAX_VALUE);
    }

    private void writeStorageClassDescriptors() {
            release.getLocalStorages().stream().forEach(storage -> {
                Map sc = ImmutableMap.builder()
                        .put("apiVersion","storage.k8s.io/v1")
                        .put("kind","StorageClass")
                        .put("metadata",ImmutableMap.builder()
                                .put("name",storage.getId())
                                .build())
                        .put("provisioner","predic8.de/local-directory")
                        .put("reclaimPolicy", "Retain")
                        .put("volumeBindingMode", "WaitForFirstConsumer")
                        .put("parameters", ImmutableMap.builder()
                                .put("baseDir",storage.getHostpath())
                                .build())
                        .build();
                writeDescriptor(storage.getId(),removeNullValuesRecursive(sc));
            });
    }

    private void writeModuleDescriptors() {
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
                                .forEach(m -> writeDescriptor(module.getId(), m));
            });
    }

    private Map removeNullValuesRecursive(Map<String,Object> m) {
        return m.entrySet().stream()
                .filter(e -> e.getValue() != Optional.empty())
                .map(e -> {
                    if(e.getValue() instanceof Map)
                        return new HashMap.SimpleEntry(e.getKey(), removeNullValuesRecursive((Map<String, Object>) e.getValue()));

                    if(e.getValue() instanceof List)
                        return new HashMap.SimpleEntry(e.getKey(), Stream.concat(((List) e.getValue()).stream().filter(entry -> entry instanceof Map).map(map -> removeNullValuesRecursive((Map) map)), ((List) e.getValue()).stream().filter(entry -> !(entry instanceof Map))).collect(Collectors.toList()));

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
                                .put("volumes",getControllerType(module) == ControllerType.STATEFULSET ? Optional.empty() : volumes(module))
                                .put("hostNetwork", release.getGateway().size() > 0 && release.getGateway().contains(module.getId()) ? true : Optional.empty())
                                .put("dnsPolicy", "None")//release.getGateway() != null && release.getGateway().equals(module.getId()) ?"ClusterFirstWithHostNet" : Optional.empty())
                                .put("dnsConfig", ImmutableMap.builder()
                                        .put("nameservers", new String[]{"10.96.0.10"})
                                        .put("searches",new String[]{"default.svc.cluster.local", "svc.cluster.local", "cluster.local"})
                                        .put("options",Arrays.asList(
                                                ImmutableMap.builder()
                                                        .put("name","ndots")
                                                        .put("value","5")
                                                        .build()
                                        ))
                                        .build())
                                .build())
                        .build())
                .putAll(getControllerType(module) == ControllerType.STATEFULSET ? ImmutableMap.builder().put("volumeClaimTemplates", createVolumeClaimTemplates(module)).build() : emptyMap())
                .build();
    }

    private Object volumes(Module module) {
        List<ImmutableMap<Object, Object>> secrets = module.getMounts().stream().filter(mount -> mount.getType() == Mount.Type.SECRET).map(mount -> ImmutableMap.builder()
                .put("name", mount.getId())
                .put("secret", ImmutableMap.builder()
                        .put("secretName", mount.getStorageName())
                        .build()
                )
                .build())
                .collect(Collectors.toList());

        List<ImmutableMap<Object, Object>> configs = module.getMounts().stream().filter(mount -> mount.getType() == Mount.Type.CONFIG).map(mount -> ImmutableMap.builder()
                .put("name", mount.getId())
                .put("configMap", ImmutableMap.builder()
                        .put("name", mount.getStorageName())
                        .build()
                )
                .build())
                .collect(Collectors.toList());

        List<ImmutableMap<Object, Object>> collect = Stream.concat(secrets.stream(), configs.stream()).collect(Collectors.toList());

        if(collect.size() == 0)
            return Optional.empty();
        return collect;
    }

    private List<Map> createVolumeClaimTemplates(Module module) {
        return module.getMounts().stream().map(mount -> {
            return ImmutableMap.builder()
                    .put("metadata", ImmutableMap.builder()
                            .put("name",mount.getId())
                            .build())
                    .put("spec",ImmutableMap.builder()
                            .put("accessModes", new String[]{"ReadWriteOnce"})
                            .put("storageClassName",mount.getStorageName())
                            .put("resources",ImmutableMap.builder()
                                    .put("requests", ImmutableMap.builder()
                                            .put("storage", mount.getStorageSize().bytes())
                                            .build())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    private Map emptyMap() {
        return ImmutableMap.builder().build();
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
                .put("env", module.getEnvironment().size() != 0 ? environment(module) : Optional.empty())
                .put("imagePullPolicy","Always")
                .put("ports", portsController(module))
                .put("volumeMounts", volumeMounts(module))
                .put("securityContext", module.getCapabilities().size() > 0 ? securityContext(module) : Optional.empty())
                .build());
    }

    private Object securityContext(Module module) {
        return ImmutableMap.builder()
                .put("capabilities", ImmutableMap.builder()
                        .put("add", module.getCapabilities().stream().collect(Collectors.toList()))
                        .build()
                )
                .build();
    }

    private Object environment(Module module) {
        if(module.getEnvironment().size() == 0)
            return Optional.empty();

        return module.getEnvironment().entrySet().stream().map(e -> ImmutableMap.builder()
                .put("name",e.getKey())
                .put("value",e.getValue())
                .build())
                .collect(Collectors.toList());
    }

    private Object volumeMounts(Module module) {
        return module.getMounts().stream().map(mount -> {
            return ImmutableMap.builder()
                    .put("name",mount.getId())
                    .put("mountPath",mount.getContainerPath())
                    .put("subPath", mount.getSingleFileName() != null ? mount.getSingleFileName() : Optional.empty())
                    .build();
        }).collect(Collectors.toList());
    }

    private Object portsController(Module module) {
        if(module.getPorts().size() == 0)
            return Optional.empty();
        return module.getPorts().stream().map(port -> ImmutableMap.builder()
                .put("containerPort",Integer.parseInt(port.getContainer()))
                .put("protocol", port.getProtocol().toUpperCase())
                .put("name",port.getName())
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

    private void writeDescriptor(String name, Map map) {
        Path p = Paths.get("tmp/release/");
        try {
            if(Files.notExists(p)) {
                Files.createDirectories(p);
            }

            Path filename = p.resolve(getFilePrefix(map) + "-" + name + "-" + map.get("kind").toString().toLowerCase() + ".yaml");

            if(Files.exists(filename))
                Files.delete(filename);

            Files.write(filename, Yaml.dump(map).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePrefix(Map map) {
        String kind = map.get("kind").toString().toLowerCase();

        Map<String,Integer> prefixes = new HashMap<>();

        Arrays.asList("namespace").stream().forEach( v -> prefixes.put(v,0));
        Arrays.asList("storageclass","clusterrole","clusterrolebinding","role","rolebinding","serviceaccount").stream().forEach( v -> prefixes.put(v,1));

        if(prefixes.containsKey(kind))
            return String.valueOf(prefixes.get(kind));

        return "99";
    }
}
