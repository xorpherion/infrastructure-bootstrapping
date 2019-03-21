package com.bornium.infrastructurebootstrapping.crypto.services;

import com.bornium.infrastructurebootstrapping.services.execution.docker.ContainerShell;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.springframework.stereotype.Service;

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

    public void createPrivateKey(String destination) {
        doInOpenSSL((shell) -> {
            System.out.println(shell.run("openssl genrsa -out private.pem 4096"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/private.pem", "temp/private.pem"));
        });
    }

    public void createCsr(String privateKey, String destination) {
        doInOpenSSL(shell -> {
            System.out.println(dockerService.putInto(shell.getContainerName(), privateKey, "/private.pem"));
            System.out.println(shell.run("openssl req -new -key private.pem -out cert.csr -subj \"/C=US/ST=Denial/L=Springfield/O=Dis/CN=www.example.com\""));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/cert.csr", "temp/cert.csr"));
        });
    }
}
