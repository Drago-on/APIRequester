package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileWriterUtilTest {

    private FileWriterUtil fileWriterUtil;
    private final String testFilePath = "test_output.txt";

    @BeforeEach
    public void setUp() {
        fileWriterUtil = new FileWriterUtil(testFilePath);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(testFilePath));
    }

    @Test
    public void testWriteToFile() throws IOException {
        String content = "Test content";
        fileWriterUtil.writeToFile(content);

        Path path = Path.of(testFilePath);
        assertTrue(Files.exists(path), "File should be created");

        List<String> lines = Files.readAllLines(path);
        assertEquals(1, lines.size(), "File should contain one line");
        assertEquals(content, lines.get(0), "Content should match the written content");
    }

    @Test
    public void testSetFilePath() throws IOException {
        String newFilePath = "new_test_output.txt";
        fileWriterUtil.setFilePath(newFilePath);

        String content = "New content";
        fileWriterUtil.writeToFile(content);

        Path newPath = Path.of(newFilePath);
        assertTrue(Files.exists(newPath), "New file should be created");

        List<String> lines = Files.readAllLines(newPath);
        assertEquals(1, lines.size(), "New file should contain one line");
        assertEquals(content, lines.get(0), "Content should match the written content");

        Files.deleteIfExists(newPath);
    }
}