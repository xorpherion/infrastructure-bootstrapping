package com.bornium.infrastructurebootstrapping.services;

import com.bornium.infrastructurebootstrapping.services.execution.CommandRunner;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CommandRunnerTest {

    @Test
    public void test() {
        CommandRunner shell = new CommandRunner();

        String res = shell.readResult(shell.run("docker ps")).getOut();
        assertNotNull(res);
        assertFalse(res.isEmpty());
    }

}