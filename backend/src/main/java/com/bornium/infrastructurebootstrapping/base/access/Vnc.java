package com.bornium.infrastructurebootstrapping.base.access;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;

import java.awt.*;
import java.util.function.Consumer;

public class Vnc {

    public static final int SHIFT = 0xffe1;
    private final VernacularConfig cfg;
    private final VernacularClient client;
    private final String host;
    private final int port;

    public Vnc(String host) {
        this(host, 5900,null);
    }

    public Vnc(String host,Consumer<Image> screenListener) {
        this(host, 5900,screenListener);
    }

    public Vnc(String host, int port, Consumer<Image> screenListener) {
        this.host = host;
        this.port = port;
        cfg = new VernacularConfig();
        cfg.setTargetFramesPerSecond(1);
        if(screenListener != null)
            cfg.setScreenUpdateListener(screenListener);
        client = new VernacularClient(cfg);
        client.start(host, port);
    }

    public void exec(String cmd) {
        for (char c : cmd.toCharArray()) {
            if (c == '@')
                type(SHIFT, '2');
            else if (c == '>')
                type(SHIFT, '>');
            else if (c == '_')
                type(SHIFT, '-');
            else
                type(c);
        }

        typeEnter();
    }

    public void type(char... chars) {
        int[] newChars = new int[chars.length];
        for (int i = 0; i < chars.length; i++)
            newChars[i] = chars[i];
        type(newChars);
    }

    public void type(int... chars) {
        /*for(int c : chars)
            client.type(c);*/
        for (int c : chars) {
            client.keyPress(c, true);
        }
        for (int i = chars.length - 1; i >= 0; i--)
            client.keyPress(chars[i], false);
    }

    public void typeEnter() {
        int i = -243; // seems to be enter, 13 does not work but 256-243=13 => -243
        client.keyPress(i, true);
        client.keyPress(i, false);
    }

    private void hold(int i) {
        client.keyPress(i, true);
    }

    public void close() {
        client.stop();
    }
}
