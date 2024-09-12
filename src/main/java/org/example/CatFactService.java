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

    private final String apiUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public CatFactService() {
        this("https://catfact.ninja/fact");
    }

    public CatFactService(String apiUrl) {
        this.apiUrl = apiUrl;
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public CatFactService(String apiUrl, HttpClient client, ObjectMapper objectMapper) {
        this.apiUrl = apiUrl;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public String fetchCatFact() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected response status: " + response.statusCode());
        }

        return parseJsonResponse(response.body());
    }

    private String parseJsonResponse(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return rootNode.get("fact").asText();
    }
}