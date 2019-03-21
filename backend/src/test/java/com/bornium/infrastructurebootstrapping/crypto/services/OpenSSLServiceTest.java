package com.bornium.infrastructurebootstrapping.crypto.services;

import com.bornium.infrastructurebootstrapping.services.execution.CommandRunner;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.junit.Test;

public class OpenSSLServiceTest {

    @Test
    public void test() {
        OpenSSLService openSSLService = new OpenSSLService(new DockerService(new CommandRunner()));
        openSSLService.createPrivateKey("temp/private.pem");
        openSSLService.createCsr("temp/private.pem", "temp/cert.csr");
    }
}