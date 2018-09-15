package com.bornium.boostrappingascode.entities.user

import com.bornium.boostrappingascode.access.Ssh

class PasswordAuthentication extends Authentication {
    final String password

    PasswordAuthentication(String password) {
        this.password = password
    }

    @Override
    def applyTo(Ssh ssh) {
        ssh.session.setPassword(password)
    }
}
