package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CatFactService {

    private static final String API_URL = "https://catfact.ninja/fact";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String fetchCatFact() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected response status: " + response.statusCode());
        }

        return parseJsonResponse(response.body());
    }

    private static String parseJsonResponse(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return rootNode.get("fact").asText();
    }
}