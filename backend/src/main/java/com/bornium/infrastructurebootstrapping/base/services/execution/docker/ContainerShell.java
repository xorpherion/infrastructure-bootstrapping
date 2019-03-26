package com.bornium.infrastructurebootstrapping.base.services.execution.docker;

import com.bornium.infrastructurebootstrapping.base.services.execution.CommandRunner;
import com.bornium.infrastructurebootstrapping.base.services.execution.Results;

public class ContainerShell {

    private final CommandRunner shell;
    final String containerName;

    public ContainerShell(String containerName) {
        this.containerName = containerName;
        shell = new CommandRunner();
    }

    public Results run(String cmd) {
        return shell.readResult(shell.run("docker exec " + containerName + " bash -c \"" + cmd + "\""));
    }

    public String getContainerName() {
        return containerName;
    }
}
