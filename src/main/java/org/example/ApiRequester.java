package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ApiRequester {

    private static final String API_URL = "https://catfact.ninja/fact";
    private static final Path OUTPUT_FILE = Path.of("response.txt");
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static int requestCount = 0;
    private static final int MAX_REQUESTS = 5;


    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (requestCount < MAX_REQUESTS) {
                    try {
                        String response = sendGetRequest();
                        String fact = parseJsonResponse(response);
                        writeToFile(fact);
                        System.out.println("Fact saved to file: " + fact);
                        requestCount++;
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    timer.cancel();
                    System.out.println("Reached maximum number of requests: " + MAX_REQUESTS);
                }
            }
        }, 0, 10000);
    }

    private static String sendGetRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected response status: " + response.statusCode());
        }

        return response.body();
    }

    private static String parseJsonResponse(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return rootNode.get("fact").asText();
    }

    private static void writeToFile(String content) throws IOException {
        Files.writeString(OUTPUT_FILE, content + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}

