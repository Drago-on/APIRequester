package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatFactServiceTest {

    private WireMockServer wireMockServer;
    private CatFactService catFactService;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
        catFactService = new CatFactService("http://localhost:8080/fact");
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchCatFactThrowsExceptionOnNon200Response() {
        WireMock.stubFor(WireMock.get(urlEqualTo("/fact"))
                .willReturn(aResponse()
                        .withStatus(404)));
        assertThrows(IOException.class, () -> catFactService.fetchCatFact());
    }

    @Test
    public void testCatFactServiceWithCustomHttpClientAndObjectMapper() throws IOException, InterruptedException {
        WireMock.stubFor(WireMock.get(urlEqualTo("/fact"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"fact\":\"Custom client fact.\"}")));
        HttpClient customClient = HttpClient.newHttpClient();
        ObjectMapper customMapper = new ObjectMapper();
        CatFactService customCatFactService = new CatFactService("http://localhost:8080/fact", customClient, customMapper);

        String result = customCatFactService.fetchCatFact();
        assertNotNull(result);
        System.out.println("Received fact: " + result);
    }

    @Test
    public void testFetchCatFactReturnsValidFact() throws IOException, InterruptedException {
        WireMock.stubFor(WireMock.get(urlEqualTo("/fact"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"fact\":\"Any valid fact.\"}")));

        String result = catFactService.fetchCatFact();
        assertNotNull(result);
        System.out.println("Received fact: " + result);
    }
}