package com.bornium.infrastructurebootstrapping.services;

import com.bornium.infrastructurebootstrapping.services.execution.CommandRunner;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.junit.Test;

public class DockerServiceTest {

    @Test
    public void test() {
        DockerService dockerService = new DockerService(new CommandRunner());

        dockerService.doInContainerFromDirectory("src/main/resources/docker/openssl/", (shell) -> {
            System.out.println(shell.run("openssl genrsa -out private.pem 4096"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/private.pem", "temp/private.pem"));
            System.out.println(shell.run("mkdir -p /crypto"));
            System.out.println(dockerService.putInto(shell.getContainerName(), "temp/private.pem", "/crypto/private.pem"));
            System.out.println(shell.run("ls"));
            System.out.println(shell.run("ls /crypto"));
        });
    }

}