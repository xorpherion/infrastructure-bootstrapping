package com.bornium.infrastructurebootstrapping.services.execution.docker;

import com.bornium.infrastructurebootstrapping.services.execution.CommandRunner;
import com.bornium.infrastructurebootstrapping.services.execution.Results;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Service
public class DockerService {

    final CommandRunner shell;

    public DockerService(CommandRunner shell) {
        this.shell = shell;
    }

    public void doInContainer(String imageName, Consumer<ContainerShell> run) {
        String name = spinUpContainer(imageName).getOut().replace("\n", "");
        if (name == null || name.isEmpty())
            throw new RuntimeException("Container not started");
        try {
            run.accept(new ContainerShell(name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            spinDownContainer(name);
        }
    }

    public void doInContainerFromDirectory(String ressource, Consumer<ContainerShell> run) {
        Path p = Paths.get(ressource + "/Dockerfile");
        String dirName = "ib/" + p.subpath(p.getNameCount() - 2, p.getNameCount() - 1).toString();
        System.out.println(buildDockerfile(p.toAbsolutePath().toString(), p.toAbsolutePath().getParent().toString(), dirName));
        doInContainer(dirName, run);
    }

    public Results buildDockerfile(String dockerfilePath, String contextPath, String tag) {
        return shell.runAndGet("docker build -t " + tag + " -f " + dockerfilePath + " " + contextPath);
    }

    public Results spinUpContainer(String imageName) {
        Results name = shell.readResult(shell.run("docker run -d --rm " + imageName));
        return name;
    }

    public void spinDownContainer(String containerName) {
        shell.run("docker rm -f " + containerName);
    }

    public Results getFrom(String containerName, String src, String dest) {
        try {
            Path fullPath = Paths.get(dest);
            if (!fullPath.toFile().isDirectory())
                fullPath = fullPath.getParent();

            if (fullPath != null && !fullPath.toFile().exists())
                Files.createDirectories(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shell.readResult(shell.run("docker cp " + containerName + ":" + src + " " + dest));
    }

    public Results putInto(String containerName, String src, String dest) {
        return shell.readResult(shell.run("docker cp " + src + " " + containerName + ":" + dest));
    }
}
