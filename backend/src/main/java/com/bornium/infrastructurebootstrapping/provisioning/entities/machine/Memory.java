package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

public class Memory {

    long tb;
    long gb;
    long mb;
    long kb;
    long b;

    public Memory() {
    }

    public Memory(long tb, long gb, long mb, long kb, long b) {
        this.tb = tb;
        this.gb = gb;
        this.mb = mb;
        this.kb = kb;
        this.b = b;
    }

    public long bytes() {
        return b +
                kb * 1024 +
                mb * 1024 * 1024 +
                gb * 1024 * 1024 * 1024 +
                tb * 1024 * 1024 * 1024 * 1024;
    }

    public long kilobytes() {
        return bytes() / 1024;
    }

    public long megabytes() {
        return kilobytes() / 1024;
    }

    public long gigabytes() {
        return megabytes() / 1024;
    }

    public long terabytes() {
        return gigabytes() / 1024;
    }

    public long getTb() {
        return tb;
    }

    public void setTb(long tb) {
        this.tb = tb;
    }

    public long getGb() {
        return gb;
    }

    public void setGb(long gb) {
        this.gb = gb;
    }

    public long getMb() {
        return mb;
    }

    public void setMb(long mb) {
        this.mb = mb;
    }

    public long getKb() {
        return kb;
    }

    public void setKb(long kb) {
        this.kb = kb;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }
}
