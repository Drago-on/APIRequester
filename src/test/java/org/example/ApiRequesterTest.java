package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ApiRequesterTest {

    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".txt");
        FileWriterUtil.setFilePath(tempFile.getAbsolutePath());
        System.out.println("Temporary file path: " + tempFile.getAbsolutePath());
        ApiRequester.resetRequestCount();
    }

    @AfterEach
    public void tearDown() {
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            assertTrue(deleted, "Temporary file should be deleted.");
        }
    }

    @Test
    public void testPerformRequest() throws IOException {
        ApiRequester.performRequest();
        assertEquals(1, ApiRequester.getRequestCount(), "Request count should be incremented.");

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line = reader.readLine();
            assertNotNull(line, "File should not be null after writing the fact.");
            assertFalse(line.isEmpty(), "File should contain a fact.");
            assertTrue(line.length() > 10, "File content should be a non-trivial fact (more than 10 characters).");
        }
    }

    @Test
    public void testStartRequestProcess() throws InterruptedException {
        ApiRequester.startRequestProcess();

        Thread.sleep(11000);

        assertEquals(2, ApiRequester.getRequestCount(), "Request count should be 2 after all requests.");
    }

    @Test
    public void testMaxRequestsReached() throws InterruptedException {
        ApiRequester.startRequestProcess();

        Thread.sleep(20000);

        assertEquals(2, ApiRequester.getRequestCount(), "Request count should reach the maximum value of 2.");

        int countBefore = ApiRequester.getRequestCount();
        Thread.sleep(5000);
        assertEquals(countBefore, ApiRequester.getRequestCount(), "No more requests should be performed.");
    }
}