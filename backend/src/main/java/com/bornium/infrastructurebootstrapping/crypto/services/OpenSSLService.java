package com.bornium.infrastructurebootstrapping.crypto.services;

import com.bornium.infrastructurebootstrapping.services.execution.docker.ContainerShell;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.function.Consumer;

@Service
public class OpenSSLService {

    final DockerService dockerService;

    public OpenSSLService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    public void doInOpenSSL(Consumer<ContainerShell> run) {
        dockerService.doInContainerFromDirectory("src/main/resources/docker/openssl/", run);
    }

    public void createPrivateKey(Path destination) {
        doInOpenSSL((shell) -> {
            System.out.println(shell.run("openssl genrsa -out private.pem 4096"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/private.pem", destination.toString()));
        });
    }

    public void createCsr(Path privateKey, Path config, X509Subject subject, Path destination) {
        doInOpenSSL(shell -> {
            System.out.println(dockerService.putInto(shell.getContainerName(), privateKey.toString(), "/private.pem"));
            System.out.println(dockerService.putInto(shell.getContainerName(), config.toString(), "/openssl.conf"));
            System.out.println(shell.run("openssl req -new -key private.pem -out cert.csr -subj '" + subject.toOpenSSL() + "'"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/cert.csr", destination.toString()));
        });
    }
}
