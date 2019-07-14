package com.bornium.infrastructurebootstrapping.base.access;

import com.bornium.infrastructurebootstrapping.provisioning.entities.credentials.Credentials;
import com.bornium.infrastructurebootstrapping.provisioning.entities.user.User;
import com.jcraft.jsch.*;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
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
        return execInternal(t -> {},t -> {},joinCommands(commands));
    }

    public String exec(Consumer<String> stdPrinter, Consumer<String> errPrinter,String... commands) {
        return execInternal(stdPrinter,errPrinter,joinCommands(commands));
    }

    public String execPrint(String... commands) {
        System.out.println("=== executing " + joinCommands(commands) + " ===");
        return exec(System.out::print,System.err::print,commands);
    }

    public String execSudo(String... commands) {
        return execInternal(t -> {},t -> {},joinCommands(Stream.of(commands).map(cmd -> "sudo " + cmd).collect(Collectors.toList()).toArray(new String[0])));
    }

    public String execSudo(Consumer<String> stdPrinter, Consumer<String> errPrinter,String... commands) {
        return execInternal(stdPrinter,errPrinter,joinCommands(Stream.of(commands).map(cmd -> "sudo " + cmd).collect(Collectors.toList()).toArray(new String[0])));
    }

    public String execSudoPrint(String... commands) {
        System.out.println("=== executing " + joinCommands(commands) + " ===");
        return execSudo(System.out::print,System.err::print,commands);
    }

    private String execInternal(Consumer<String> stdPrinter, Consumer<String> errPrinter, String command) {
        makeSureConnected();
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
                    String t = new String(tmp, 0, i);
                    stdPrinter.accept(t);
                    result += t;
                }

                while (errStream.available() > 0) {
                    int i = errStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String t = new String(tmp, 0, i);
                    errPrinter.accept(t);
                    result += t;
                }

                if ((channel).isClosed()) {
                    if (inputStream.available() > 0) continue;
                    if (errStream.available() > 0) continue;
                    String t = "exit-status: " + ((ChannelExec) channel).getExitStatus() + System.lineSeparator();
                    stdPrinter.accept(t);
                    result += t;
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

    private void makeSureConnected() {
        if (session == null)
            connect();
    }

    private String joinCommands(String... commands) {
        return String.join(";", commands);
    }

    public void copyToRemote(String source, String destination) throws FileNotFoundException, JSchException, SftpException {
        makeSureConnected();
        ChannelSftp channel = null;
        channel = (ChannelSftp)session.openChannel("sftp");
        try {
            channel.connect();
            File localFile = Paths.get(source).toFile();
            Path d = Paths.get(destination);
            if(d.getNameCount() > 1)
                channel.cd(channel.pwd() + "/" + d.subpath(0,d.getNameCount()-1).normalize().toString().replace("\\","/"));
            channel.put(new FileInputStream(localFile), d.getName(d.getNameCount()-1).toString());
        }finally {
            channel.disconnect();
        }
    }

    public void copyToLocal(String source, String destination) throws IOException, JSchException, SftpException {
        makeSureConnected();
        ChannelSftp channel = null;
        channel = (ChannelSftp)session.openChannel("sftp");
        try {
            channel.connect();
            Files.copy(channel.get(source),Paths.get(destination));
        }finally {
            channel.disconnect();
        }
    }

    public void disconnect() {
        if(session != null)
            session.disconnect();
        session = null;
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
