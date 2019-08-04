package com.bornium.infrastructurebootstrapping.provisioning.entities.machine.passthrough;

public class PciPassthrough {

    String domain;
    String bus;
    String slot;
    String function;

    public PciPassthrough(String domain, String bus, String slot, String function) {
        this.domain = domain;
        this.bus = bus;
        this.slot = slot;
        this.function = function;
    }

    public String getDomain() {
        return domain;
    }

    public String getBus() {
        return bus;
    }

    public String getSlot() {
        return slot;
    }

    public String getFunction() {
        return function;
    }
}
