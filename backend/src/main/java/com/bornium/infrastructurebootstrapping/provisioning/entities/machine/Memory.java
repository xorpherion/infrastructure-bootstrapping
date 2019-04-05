package com.bornium.infrastructurebootstrapping.provisioning.entities.machine;

public class Memory {

    Long tb;
    Long gb;
    Long mb;
    Long kb;
    Long b;

    public Memory(Long tb, Long gb, Long mb, Long kb, Long b) {
        this.tb = tb;
        this.gb = gb;
        this.mb = mb;
        this.kb = kb;
        this.b = b;
    }

    private long unwrap(Long l){
        return l != null? l.longValue() : 0;
    }

    public long bytes() {
        return  unwrap(b) +
                unwrap(kb) * 1024 +
                unwrap(mb) * 1024 * 1024 +
                unwrap(gb) * 1024 * 1024 * 1024 +
                unwrap(tb) * 1024 * 1024 * 1024 * 1024;
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

    public Long getTb() {
        return tb;
    }

    public Long getGb() {
        return gb;
    }

    public Long getMb() {
        return mb;
    }

    public Long getKb() {
        return kb;
    }

    public Long getB() {
        return b;
    }
}
