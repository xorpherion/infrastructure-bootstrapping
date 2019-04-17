package com.bornium.infrastructurebootstrapping.provisioning.entities.credentials;

import com.bornium.infrastructurebootstrapping.base.access.Ssh;
import com.jcraft.jsch.JSchException;

import java.nio.file.Paths;

public class PrivateKeyCredentials extends Credentials {
    private final String privateKeyFilePath;
    private final String keyPassword;

    public PrivateKeyCredentials(String id, String privateKeyFilePath, String keyPassword) {
        super(id);
        this.privateKeyFilePath = privateKeyFilePath;
        this.keyPassword = keyPassword;
    }

    private String unwrapEnv(String password) {
        if(!password.startsWith("$"))
            return password;

        return System.getenv(password.substring(1));
    }

    @Override
    public void applyTo(Ssh ssh) {
        try {
            ssh.getJsch().addIdentity(Paths.get(System.getProperty("user.home")+ "/" + privateKeyFilePath).toAbsolutePath().toString(),unwrapPassword());
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public final String unwrapPassword(){
        return unwrapEnv(getKeyPassword());
    }

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }
}
