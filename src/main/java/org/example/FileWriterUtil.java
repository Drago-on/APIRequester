package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriterUtil {

    private static final Path OUTPUT_FILE = Path.of("response.txt");

    public static void writeToFile(String content) throws IOException {
        Files.writeString(OUTPUT_FILE, content + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}