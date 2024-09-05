package org.example;

import java.util.Timer;
import java.util.TimerTask;

public class ApiRequester {

    private static final int MAX_REQUESTS = 2;
    private static int requestCount = 0;
    private static Timer timer;

    public static void main(String[] args) {
        startRequestProcess();
    }

    public static void startRequestProcess() {
        timer = new Timer();
        requestCount = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (requestCount < MAX_REQUESTS) {
                    performRequest();
                } else {
                    timer.cancel();
                    System.out.println("Reached maximum number of requests: " + MAX_REQUESTS);
                }
            }
        }, 0, 10000);
    }

    public static void performRequest() {
        try {
            String fact = CatFactService.fetchCatFact();
            FileWriterUtil.writeToFile(fact);
            System.out.println("Fact saved to file: " + fact);
            requestCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRequestCount() {
        return requestCount;
    }

    public static void resetRequestCount() {
        requestCount = 0;
    }
}