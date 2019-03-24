package com.bornium.infrastructurebootstrapping.crypto.services.openssl;

public class X509Subject {

    String c;
    String st;
    String l;
    String o;
    String cn;
    String oun;
    String email;

    public X509Subject(String c, String st, String l, String o, String cn) {
        this.c = c;
        this.st = st;
        this.l = l;
        this.o = o;
        this.cn = cn;
    }

    public String toOpenSSLConfig() {
        return "countryName=" + c + "\n" +
                "stateOrProvinceName=" + st + "\n" +
                "localityName=" + l + "\n" +
                "0.organizationName=" + o + "\n" +
                "commonName=" + cn + "\n";
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
