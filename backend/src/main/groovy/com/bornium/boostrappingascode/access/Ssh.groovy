package com.bornium.boostrappingascode.access

import com.bornium.boostrappingascode.entities.user.User
import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

import java.util.stream.Collectors
import java.util.stream.Stream

class Ssh {
    public static final int SSH_TIMEOUT = 30000

    String host
    int port
    User user
    OutputStream output
    OutputStream error
    JSch jsch;
    Session session
    //ChannelExec channel

    Ssh(String host, int port, User user) {
        this.host = host
        this.port = port
        this.user = user

        //input = System.in
        output = System.out
        error = System.err
    }

    void connect(){
        try {
            jsch = new JSch();
            session = jsch.getSession(user.name, host, port)
            user.authentication.applyTo(this)

            // TODO remove
            session.setConfig("StrictHostKeyChecking", "no")

            //session.setInputStream(input)
            session.setOutputStream(output)

            session.connect(SSH_TIMEOUT)
        }catch(Exception e){
            e.printStackTrace()
            disconnect()
        }
    }

    def exec(String... commands){
        String command = String.join(";",commands)
        println "=== executing $command ==="
        def channel
        try {
            channel = session.openChannel("exec") as ChannelExec
            channel.setErrStream(error)
            channel.setCommand(command)
            channel.connect()

            InputStream inputStream = channel.getInputStream()
            OutputStream outputStream = channel.getOutputStream()

            byte[] tmp=new byte[1024];
            while(true){
                while(inputStream.available()>0){
                    int i=inputStream.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    if(inputStream.available()>0) continue;
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
        }catch(Exception e){
            e.printStackTrace()
        }finally{
            println "==="
            channel?.disconnect()
        }
    }

    def execSudo(String... commands){
        return exec(Stream.of(commands).map {command -> "sudo " + command}.collect(Collectors.toList()).toArray(new String[0]))
    }

    void disconnect(){
        session?.disconnect()
    }
}
