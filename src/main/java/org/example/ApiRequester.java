package org.example;

import java.util.Timer;
import java.util.TimerTask;

public class ApiRequester {

    private static final int MAX_REQUESTS = 2;
    private int requestCount = 0;
    private Timer timer;
    private final CatFactService catFactService;
    private final FileWriterUtil fileWriterUtil;

    public ApiRequester(CatFactService catFactService, FileWriterUtil fileWriterUtil) {
        this.catFactService = catFactService;
        this.fileWriterUtil = fileWriterUtil;
    }

    public void startRequestProcess() {
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

    public void performRequest() {
        try {
            String fact = catFactService.fetchCatFact();
            fileWriterUtil.writeToFile(fact);
            System.out.println("Fact saved to file: " + fact);
            requestCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void resetRequestCount() {
        requestCount = 0;
    }
}