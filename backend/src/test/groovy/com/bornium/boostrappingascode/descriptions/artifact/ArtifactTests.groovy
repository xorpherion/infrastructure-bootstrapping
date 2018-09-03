package com.bornium.boostrappingascode.descriptions.artifact

import org.junit.Test

import static com.bornium.boostrappingascode.descriptions.Util.log

class ArtifactTests {

    @Test
    void createDockerArtifact(){
        Artifact artifact = Artifact.docker("test-artifact","Test artifact",Source.dockerRegistry("docker-registry"),"latest")
        log(artifact)
    }

    @Test
    void createMavenArtifact(){

    }

    @Test
    void createGradleArtifact(){

    }
}
