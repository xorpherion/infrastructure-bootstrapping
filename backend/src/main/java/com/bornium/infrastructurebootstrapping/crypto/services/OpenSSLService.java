package com.bornium.infrastructurebootstrapping.crypto.services;

import com.bornium.infrastructurebootstrapping.crypto.services.openssl.OpenSSLExtension;
import com.bornium.infrastructurebootstrapping.crypto.services.openssl.SAN;
import com.bornium.infrastructurebootstrapping.crypto.services.openssl.X509Subject;
import com.bornium.infrastructurebootstrapping.services.execution.docker.ContainerShell;
import com.bornium.infrastructurebootstrapping.services.execution.docker.DockerService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Service
public class OpenSSLService {

    boolean debug = true;
    final DockerService dockerService;

    public OpenSSLService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    public void doWithOpenSSL(Consumer<ContainerShell> run) {
        dockerService.doInContainerFromDirectory("src/main/resources/files/docker/openssl/", run);
    }

    Path templateConfig(Path csrConfig, X509Subject subject, SAN san) {
        try {
            String content = new String(Files.readAllBytes(csrConfig));
            Path temp = Files.createTempFile("ib", ".conf");

            content = replaceSAN(san, replaceSubject(subject, content));

            if (debug)
                System.out.println(content);
            Files.write(temp, content.getBytes());
            temp.toFile().deleteOnExit();
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String replaceSubject(X509Subject subject, String content) {
        if (subject == null || subject.toOpenSSLConfig().isEmpty())
            return content.replace("[ req_distinguished_name ]", "").replace("${subject}", "");

        return content
                .replace("${subject}", subject.toOpenSSLConfig());
    }

    private String replaceSAN(SAN san, String contentNew) {
        if (san == null || san.toOpenSSLConfig().isEmpty())
            return contentNew
                    .replace("[ alt_names ]", "")
                    .replace("${san}", "")
                    .replace("req_extensions      = req_ext", "");

        return contentNew.replace("${san}", san.toOpenSSLConfig());
    }

    public void createPrivateKey(Path destination) {
        doWithOpenSSL((shell) -> {
            System.out.println(shell.run("openssl genrsa -out private.pem 4096"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/private.pem", destination.toString()));
        });
    }

    public void createCsr(Path privateKey, Path config, X509Subject subject, SAN san, Path destination) {
        doWithOpenSSL(shell -> {
            System.out.println(dockerService.putInto(shell.getContainerName(), privateKey.toString(), "/private.pem"));
            System.out.println(dockerService.putInto(shell.getContainerName(), templateConfig(config, subject, san).toString(), "/openssl.conf"));
            System.out.println(shell.run("openssl req -new -key private.pem -out cert.csr -config openssl.conf"));
            if (debug)
                System.out.println(shell.run("openssl req -in cert.csr -noout -text"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/cert.csr", destination.toString()));
        });
    }

    public void createSelfSignedCert(Path privateKey, Path config, X509Subject subject, SAN san, OpenSSLExtension extension, Path destination) {
        doWithOpenSSL(shell -> {
            System.out.println(dockerService.putInto(shell.getContainerName(), privateKey.toString(), "/private.pem"));
            System.out.println(dockerService.putInto(shell.getContainerName(), templateConfig(config, subject, san).toString(), "/openssl.conf"));
            System.out.println(shell.run("openssl req -extensions " + extension.getValue() + " -new -x509 -key private.pem -out cert.pem -config openssl.conf"));
            if (debug)
                System.out.println(shell.run("openssl x509 -in cert.pem -noout -text"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/cert.pem", destination.toString()));
        });
    }

    public void signCsr(Path privateKey, Path cert, Path config, Path csr, OpenSSLExtension extension, Path destination) {
        doWithOpenSSL(shell -> {
            System.out.println(dockerService.putInto(shell.getContainerName(), privateKey.toString(), "/ca-key.pem"));
            System.out.println(dockerService.putInto(shell.getContainerName(), cert.toString(), "/ca-cert.pem"));
            System.out.println(dockerService.putInto(shell.getContainerName(), templateConfig(config, null, null).toString(), "/openssl.conf"));
            System.out.println(dockerService.putInto(shell.getContainerName(), csr.toString(), "/cert.csr"));
            System.out.println(shell.run("openssl x509 -req -extfile openssl.conf -extensions " + extension.getValue() + " -in cert.csr -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out cert.pem"));
            if (debug)
                System.out.println(shell.run("openssl x509 -in cert.pem -noout -text"));
            System.out.println(dockerService.getFrom(shell.getContainerName(), "/cert.pem", destination.toString()));
        });
    }

    public void createCA(Path caKey, Path config, Path destination) {

    }
}
