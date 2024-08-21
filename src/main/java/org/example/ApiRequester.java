package org.example;

import java.util.Timer;
import java.util.TimerTask;

public class ApiRequester {

    private static final int MAX_REQUESTS = 5;
    private static int requestCount = 0;

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (requestCount < MAX_REQUESTS) {
                    try {
                        String fact = CatFactService.fetchCatFact();
                        FileWriterUtil.writeToFile(fact);
                        System.out.println("Fact saved to file: " + fact);
                        requestCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    timer.cancel();
                    System.out.println("Reached maximum number of requests: " + MAX_REQUESTS);
                }
            }
        }, 0, 10000);
    }
}