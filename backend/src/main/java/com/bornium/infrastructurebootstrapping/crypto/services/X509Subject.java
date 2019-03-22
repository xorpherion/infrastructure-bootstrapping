package com.bornium.infrastructurebootstrapping.crypto.services;

public class X509Subject {

    String c;
    String st;
    String l;
    String o;
    String cn;

    public X509Subject(String c, String st, String l, String o, String cn) {
        this.c = c;
        this.st = st;
        this.l = l;
        this.o = o;
        this.cn = cn;
    }

    public String toOpenSSL() {
        return "/C=" + getC() + "/ST=" + getSt() + "/L=" + getL() + "/O=" + getO() + "/CN=" + getCn();
    }

    public String getC() {
        return c;
    }

    public String getSt() {
        return st;
    }

    public String getL() {
        return l;
    }

    public String getO() {
        return o;
    }

    public String getCn() {
        return cn;
    }
}
