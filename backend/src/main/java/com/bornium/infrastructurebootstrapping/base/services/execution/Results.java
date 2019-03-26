package com.bornium.infrastructurebootstrapping.base.services.execution;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.util.StreamUtils.copyToString;

public class Results {

    private String out;
    private String err;

    public Results(String out, String err) {
        this.out = out;
        this.err = err;
    }

    public Results(InputStream out, InputStream err) throws IOException {
        this(copyToString(out, Charset.defaultCharset()), copyToString(err, Charset.defaultCharset()));
    }

    public Results(Process p) throws IOException {
        this(p.getInputStream(), p.getErrorStream());
    }

    public String getOut() {
        return out;
    }

    public String getErr() {
        return err;
    }

    @Override
    public String toString() {
        return "Results{" + "\n" +
                "out='\n" + out + '\'' +
                ", err='" + err + '\'' + "\n" +
                '}';
    }
}
