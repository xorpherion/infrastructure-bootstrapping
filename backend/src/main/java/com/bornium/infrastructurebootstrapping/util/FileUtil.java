package com.bornium.infrastructurebootstrapping.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static String readFileIntoString(String filename) throws IOException {
        return String.join(
                System.lineSeparator(),
                Files.readAllLines(
                        Paths.get(filename
                        )
                )
        );
    }
}
