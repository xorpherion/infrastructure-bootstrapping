package com.bornium.boostrappingascode

import com.bornium.boostrappingascode.access.Ssh
import com.bornium.boostrappingascode.entities.user.PasswordAuthentication
import com.bornium.boostrappingascode.entities.user.User
import org.junit.Test

class DevMethods {
    String user = System.getenv("USER")
    String pwd = System.getenv("PWD")
    String host = System.getenv("IP")
    int port = Integer.parseInt(System.getenv("PORT"))

    @Test
    void sshTest() {
        Ssh ssh = new Ssh(host, port, new User(user, new PasswordAuthentication(pwd)))

        ssh.connect()
        ssh.execSudo("virsh list --all")
        ssh.disconnect()
    }

    @Test
    void sshInit() {
        Ssh ssh = new Ssh(host, port, new User(user, new PasswordAuthentication(pwd)))

        ssh.connect()
        ssh.exec("date", "date")
        ssh.execSudo("date", "date")
        ssh.exec("whoami")
        ssh.execSudo("whoami")
        ssh.execSudo("whoami")
        ssh.exec("whoami", "sudo whoami")

        ssh.disconnect()
    }
}
