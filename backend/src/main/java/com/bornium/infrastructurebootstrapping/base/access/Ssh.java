package com.bornium.infrastructurebootstrapping.base.access;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ssh {

    public static final int SSH_TIMEOUT = 30000;
    private String host;
    private int port;
    private String username;
    private Credentials credentials;
    private OutputStream output;
    private OutputStream error;
    private JSch jsch;
    private Session session;


    public Ssh(String host, int port, String username, Credentials credentials) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.credentials = credentials;

        //input = System.in
        output = System.out;
        error = System.err;
    }

    public void connect() {
        try {
            jsch = new JSch();
            session = jsch.getSession(username, host, port);
            credentials.applyTo(this);

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

    public String exec(String... commands) {
        return execInternal(joinCommands(commands));
    }

    public String execPrint(String... commands) {
        System.out.println("=== executing " + joinCommands(commands) + " ===");
        String res = exec(commands);
        System.out.println(res);
        return res;
    }

    public String execSudo(String... commands) {
        return execInternal(joinCommands(Stream.of(commands).map(cmd -> "sudo " + cmd).collect(Collectors.toList()).toArray(new String[0])));
    }

    public String execSudoPrint(String... commands) {
        System.out.println("=== executing " + joinCommands(commands) + " ===");
        String res = execSudo(commands);
        System.out.println(res);
        return res;
    }

    private String execInternal(String command) {
        if (session == null)
            connect();
        ChannelExec channel = null;
        String result = "";
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setErrStream(error);
            channel.setCommand(command);
            channel.connect();

            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
            InputStream errStream = channel.getErrStream();

            byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result += (new String(tmp, 0, i));
                }

                while (errStream.available() > 0) {
                    int i = errStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result += (new String(tmp, 0, i));
                }

                if ((channel).isClosed()) {
                    if (inputStream.available() > 0) continue;
                    if (errStream.available() > 0) continue;
                    result += ("exit-status: " + ((ChannelExec) channel).getExitStatus());
                    break;
                }

                try {
                    Thread.sleep(1);
                } catch (Exception ee) {
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null)
                channel.disconnect();
            return result;
        }
    }

    private String joinCommands(String... commands) {
        return String.join(";", commands);
    }

    public void disconnect() {
        session.disconnect();
    }

    public static String quote(String str){
        return "'" + str+ "'";
    }

    public static String dquote(String str){
        return "\"" + str+ "\"";
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
