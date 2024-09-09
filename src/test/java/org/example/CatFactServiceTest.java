package org.example;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CatFactServiceTest {

    @Test
    public void testParseJsonResponse() {
        String jsonResponse = "{\"fact\": \"Cats have five toes on their front paws.\"}";
        try {
            Method method = CatFactService.class.getDeclaredMethod("parseJsonResponse", String.class);
            method.setAccessible(true);
            String fact = (String) method.invoke(null, jsonResponse);
            assertEquals("Cats have five toes on their front paws.", fact, "Parsed fact should match the expected output.");
        } catch (Exception e) {
            fail("Exception during reflection access: " + e.getMessage());
        }
    }

    @Test
    public void testApiRequesterMaxRequestsReached() throws InterruptedException {
        ApiRequester.startRequestProcess();
        Thread.sleep(11000);
        assertEquals(2, ApiRequester.getRequestCount(), "Request count should reach the maximum value of 2.");
    }
}