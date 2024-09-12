package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.*;

public class ApiRequesterTest {

    private WireMockServer wireMockServer;
    private ApiRequester apiRequester;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        WireMock.stubFor(WireMock.get(urlEqualTo("/fact"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"fact\":\"Cats have retractable claws.\"}")));

        tempFile = Files.createTempFile("test_cats", ".txt");
        FileWriterUtil fileWriterUtil = new FileWriterUtil(tempFile.toString());
        CatFactService catFactService = new CatFactService("http://localhost:8080/fact");
        apiRequester = new ApiRequester(catFactService, fileWriterUtil);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testApiRequesterStartRequestProcess() throws InterruptedException, IOException {
        apiRequester.startRequestProcess();
        Thread.sleep(15000);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(2, lines.size());

        assertTrue(lines.contains("Cats have retractable claws."));
    }

    @Test
    public void testApiRequesterGetRequestCount() {
        assertEquals(0, apiRequester.getRequestCount());

        apiRequester.performRequest();
        apiRequester.performRequest();

        assertEquals(2, apiRequester.getRequestCount());
    }

    @Test
    public void testStartRequestProcessMaxRequestsReached() throws InterruptedException {
        apiRequester.resetRequestCount();
        apiRequester.startRequestProcess();
        Thread.sleep(25000);
        assertEquals(2, apiRequester.getRequestCount());
    }

    @Test
    public void testPerformRequestWithException() {
        CatFactService testCatFactService = new TestCatFactService();
        FileWriterUtil testFileWriterUtil = new FileWriterUtil(tempFile.toString());
        ApiRequester apiRequester = new ApiRequester(testCatFactService, testFileWriterUtil);

        try {
            apiRequester.performRequest();
            assertEquals(0, apiRequester.getRequestCount());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testResetRequestCount() {
        apiRequester.performRequest();
        apiRequester.performRequest();
        apiRequester.performRequest();

        assertEquals(3, apiRequester.getRequestCount());

        apiRequester.resetRequestCount();

        assertEquals(0, apiRequester.getRequestCount());
    }
}