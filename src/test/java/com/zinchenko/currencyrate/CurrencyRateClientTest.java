package com.zinchenko.currencyrate;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.zinchenko.currencyrate.dto.CurrencyRate;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class CurrencyRateClientTest {

    private CurrencyRateClient currencyRateClient;

    private MockWebServer mockBackEnd;
    private String baseUrl;

    @BeforeEach
    protected final void initialization() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        currencyRateClient = new CurrencyRateClient(baseUrl);
    }

    @AfterEach
    protected final void cleanUp() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void getCurrencyRatesTest() throws InterruptedException {
        Integer currencyCodeA = 980;
        Integer currencyCodeB = 981;
        Double rateBuy = 1.50D;

        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        [
                        {
                           "currencyCodeA": %s,
                           "currencyCodeB": %s,
                           "rateBuy": %s
                        }
                        ]
                        """.formatted(currencyCodeA, currencyCodeB, rateBuy))
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value()));

        List<CurrencyRate> currencyRates = currencyRateClient.getCurrencyRates();

        assertEquals(currencyCodeA, currencyRates.get(0).getCurrencyCodeA());
        assertEquals(currencyCodeB, currencyRates.get(0).getCurrencyCodeB());
        assertEquals(rateBuy, currencyRates.get(0).getRateBuy());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        assertEquals("/bank/currency", recordedRequest.getPath());
    }
}
