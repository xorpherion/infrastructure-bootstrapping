package com.bornium.infrastructurebootstrapping.crypto.services;

import com.bornium.infrastructurebootstrapping.services.execution.CommandRunner;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.junit.Test;

import java.nio.file.Paths;

public class OpenSSLServiceTest {

    @Test
    public void test() {
        OpenSSLService openSSLService = new OpenSSLService(new DockerService(new CommandRunner()));
        openSSLService.createPrivateKey(Paths.get("temp/private.pem"));
        openSSLService.createCsr(Paths.get("temp/private.pem"),
                Paths.get("src/main/resources/crypto/openssl-server.conf"),
                new X509Subject("DE", "Northrine-Westphalia", "Bonn", "Bornium Security", "bornium.com"),
                Paths.get("temp/cert.csr"));
    }
}