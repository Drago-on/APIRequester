package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriterUtil {

    private Path outputPath;

    public FileWriterUtil(String filePath) {
        this.outputPath = Path.of(filePath);
    }

    public void writeToFile(String content) throws IOException {
        Files.writeString(outputPath, content + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("Data written to file: " + outputPath);
    }

    public void setFilePath(String filePath) {
        outputPath = Path.of(filePath);
        System.out.println("File path set to: " + outputPath);
    }
}