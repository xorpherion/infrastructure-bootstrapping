package com.bornium.infrastructurebootstrapping.base.services.execution.docker;

import org.junit.Test;

public class ContainerShellTest {


    @Test
    public void test() {
        ContainerShell containerShell = new ContainerShell("optimistic_clarke");
        System.out.println(containerShell.run("echo hello"));
    }
}