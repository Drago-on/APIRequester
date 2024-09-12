package org.example;

import java.io.IOException;

public class TestCatFactService extends CatFactService {
    @Override
    public String fetchCatFact() throws IOException {
        throw new IOException("Test exception");
    }
}
