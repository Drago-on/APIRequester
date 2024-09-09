package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileWriterUtilTest {

    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".txt");
        FileWriterUtil.setFilePath(tempFile.getAbsolutePath());
        System.out.println("Temporary file path set to: " + tempFile.getAbsolutePath());
    }

    @AfterEach
    public void tearDown() {
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            if (!deleted) {
                System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
            }
        }
    }

    @Test
    public void testWriteToFileSuccess() throws IOException {
        String testContent = "Test content";

        FileWriterUtil.writeToFile(testContent);

        String fileContent = readFileContent(tempFile);
        assertEquals(testContent, fileContent, "File should contain the written content.");
    }

    @Test
    public void testSetFilePath() throws IOException {
        String newFilePath = tempFile.getAbsolutePath();
        FileWriterUtil.setFilePath(newFilePath);

        String testContent = "Another test content";
        FileWriterUtil.writeToFile(testContent);

        String fileContent = readFileContent(tempFile);
        assertEquals(testContent, fileContent, "File should contain the new written content.");
    }

    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}