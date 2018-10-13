package com.bornium.infrastructurebootstrapping.access;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;

public class Vnc {

    private final VernacularConfig cfg;
    private final VernacularClient client;
    private final String host;
    private final int port;

    public Vnc(String host) {
        this(host, 5900);
    }

    public Vnc(String host, int port) {
        this.host = host;
        this.port = port;
        cfg = new VernacularConfig();
        client = new VernacularClient(cfg);
        client.start(host, port);
    }

    public void exec(String cmd) {
        for (char c : cmd.toCharArray()) {
            type(c);
        }
        typeEnter();
    }

    public void type(char c) {
        client.keyPress((int) c, true);
        client.keyPress((int) c, false);
    }

    public void typeEnter() {
        int i = -243; // seems to be enter, 13 does not work but 256-243=13 => -243
        client.keyPress(i, true);
        client.keyPress(i, false);
    }

    public void close() {
        client.stop();
    }
}
