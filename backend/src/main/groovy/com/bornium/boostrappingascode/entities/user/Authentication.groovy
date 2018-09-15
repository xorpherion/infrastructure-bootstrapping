package com.bornium.boostrappingascode.entities.user

import com.bornium.boostrappingascode.access.Ssh

abstract class Authentication {
    abstract applyTo(Ssh ssh)
}
