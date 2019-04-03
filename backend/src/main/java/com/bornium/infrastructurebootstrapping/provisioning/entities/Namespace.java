package com.bornium.infrastructurebootstrapping.provisioning.entities;

import javax.persistence.Entity;

public class Namespace extends Base {
    public static final String GLOBAL = "global";

    public Namespace(String id) {
        super(new BaseId(id,id));
    }
}
