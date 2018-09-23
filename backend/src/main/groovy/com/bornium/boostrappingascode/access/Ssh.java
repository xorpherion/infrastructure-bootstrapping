package com.bornium.boostrappingascode.access;

import com.bornium.boostrappingascode.entities.user.User;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ssh {
    public Ssh(String host, int port, User user) {
        this.host = host;
        this.port = port;
        this.user = user;

        //input = System.in
        output = System.out;
        error = System.err;
    }

    public void connect() {
        try {
            jsch = new JSch();
            session = jsch.getSession(user.getName(), host, port);
            user.getAuthentication().applyTo(this);

            // TODO remove
            session.setConfig("StrictHostKeyChecking", "no");

            //session.setInputStream(input)
            session.setOutputStream(output);

            session.connect(SSH_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }

    }

    public void exec(String... commands) {
        String command = String.join(";", commands);
        DefaultGroovyMethods.println(this, "=== executing " + command + " ===");
        ChannelExec channel = null;
        try {
            channel = DefaultGroovyMethods.asType(session.openChannel("exec"), ChannelExec.class);
            channel.setErrStream(error);
            channel.setCommand(command);
            channel.connect();

            InputStream inputStream = ((ChannelExec) channel).getInputStream();
            OutputStream outputStream = ((ChannelExec) channel).getOutputStream();

            byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }

                if (((ChannelExec) channel).isClosed()) {
                    if (inputStream.available() > 0) continue;
                    System.out.println("exit-status: " + ((ChannelExec) channel).getExitStatus());
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DefaultGroovyMethods.println(this, "===");
            if (channel != null)
                channel.disconnect();
        }

    }

    public void execSudo(String... commands) {
        exec(Stream.of(commands).map(cmd -> "sudo" + cmd).collect(Collectors.toList()).toArray(new String[0]));
    }

    public void disconnect() {
        session.disconnect();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public OutputStream getError() {
        return error;
    }

    public void setError(OutputStream error) {
        this.error = error;
    }

    public JSch getJsch() {
        return jsch;
    }

    public void setJsch(JSch jsch) {
        this.jsch = jsch;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static final int SSH_TIMEOUT = 30000;
    private String host;
    private int port;
    private User user;
    private OutputStream output;
    private OutputStream error;
    private JSch jsch;
    private Session session;
}
