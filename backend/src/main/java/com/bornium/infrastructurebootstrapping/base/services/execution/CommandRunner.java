package com.bornium.infrastructurebootstrapping.base.services.execution;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandRunner {

    final Supplier<String> cmdRunner;
    private final Function<String, String> backgroundWrap;

    public CommandRunner() {
        String os = System.getProperty("os.name").toLowerCase();
        cmdRunner = getRunner(os);
        backgroundWrap = getBackgroundWrapper(os);
    }

    private Function<String, String> getBackgroundWrapper(String os) {
        return os.contains("win") ? windowsBackgroundWrap() : linuxBackgroundWrap();
    }

    private Supplier<String> getRunner(String os) {
        return os.contains("win") ? windowsCmdRunner() : linuxCmdRunner();
    }

    private Supplier<String> linuxCmdRunner() {
        return () -> "bash -c";
    }

    private Supplier<String> windowsCmdRunner() {
        return () -> "cmd /C";
    }

    private Function<String, String> linuxBackgroundWrap() {
        return (cmd) -> cmd + "&";
    }

    private Function<String, String> windowsBackgroundWrap() {
        return (cmd) -> "Start /B " + cmd + "-v";
    }

    public Process run(String cmd) {
        System.out.println("Running: " + cmd);
        try {
            return new ProcessBuilder().command(Stream.concat(Stream.of(cmdRunner.get().split(" ")), Stream.of(cmd)).collect(Collectors.toList())).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Results runAndGet(String cmd) {
        return readResult(run(cmd));
    }

    public Results readResult(Process p) {
        try {
            return new Results(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runBackground(String cmd) {
        run(backgroundWrap.apply(cmd));
    }
}
