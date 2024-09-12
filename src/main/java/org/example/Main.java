package org.example;

public class Main {
    public static void main(String[] args) {
        CatFactService catFactService = new CatFactService();
        FileWriterUtil fileWriterUtil = new FileWriterUtil("response.txt");
        ApiRequester apiRequester = new ApiRequester(catFactService, fileWriterUtil);

        apiRequester.startRequestProcess();
    }
}