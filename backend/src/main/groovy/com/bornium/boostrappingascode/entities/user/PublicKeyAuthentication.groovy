package com.bornium.boostrappingascode.entities.user

import com.bornium.boostrappingascode.access.Ssh

class PublicKeyAuthentication extends Authentication {
    @Override
    def applyTo(Ssh ssh) {
        return null
    }
}
