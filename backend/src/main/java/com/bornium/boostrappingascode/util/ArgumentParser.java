package com.bornium.boostrappingascode.util;

import java.nio.file.Path;

public class ArgumentParser {
    public ArgumentParser(String[] rawArgs) {
    }

    public Path getCloudsFile() {
        return cloudsFile;
    }

    public void setCloudsFile(Path cloudsFile) {
        this.cloudsFile = cloudsFile;
    }

    private Path cloudsFile;
}
