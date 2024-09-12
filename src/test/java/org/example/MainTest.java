package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    public void testMainMethod() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Path outputPath = Path.of("response.txt");
        if (Files.exists(outputPath)) {
            Files.delete(outputPath);
        }

        String[] args = {};
        Thread mainThread = new Thread(() -> Main.main(args));
        mainThread.start();

        Thread.sleep(25000);

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Reached maximum number of requests"), "The message indicating maximum requests reached was not found in the output");

        assertTrue(Files.exists(outputPath), "The response.txt file was not created");

        List<String> lines = Files.readAllLines(outputPath);
        assertFalse(lines.isEmpty(), "The response.txt file should contain at least one line");

        System.setOut(originalOut);
    }
}