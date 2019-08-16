package com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough;

public class FileSystem {

    String source;
    String target;
    String vmPath;

    public FileSystem(String source, String target, String vmPath) {
        this.source = source;
        this.target = target;
        this.vmPath = vmPath;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getVmPath() {
        return vmPath;
    }
}
