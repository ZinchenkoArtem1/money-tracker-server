package com.zinchenko.monobank.integration;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
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
class MonobankClientTest {

    private final String getClientInfoPath = "/get-client-info";
    private final String getStatementsPath = "/get-statements-path";
    private final String tokenHeaderName = "token-header-name";

    private MonobankClient monobankClient;

    private MockWebServer mockBackEnd;
    private String baseUrl;

    @BeforeEach
    protected final void initialization() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        this.monobankClient = new MonobankClient(baseUrl, getClientInfoPath, getStatementsPath, tokenHeaderName);
    }

    @AfterEach
    protected final void cleanUp() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void getClientInfoTest() throws InterruptedException {
        String token = "token";
        String clientId = "clientId";
        String name = "name";
        String accountId = "1";
        Long accountBalance = 100L;

        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        {
                           "clientId": "%s",
                           "name": "%s",
                           "accounts": [
                            {
                                "id": "%s",
                                "balance": %s
                            }
                           ]
                        }
                        """.formatted(clientId, name, accountId, accountBalance))
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value()));

        GetClientInfoResponse getClientInfoResponse = monobankClient.getClientInfo(token);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        assertEquals(getClientInfoPath, recordedRequest.getPath());
        assertEquals(token, recordedRequest.getHeader(tokenHeaderName));
        assertEquals(clientId, getClientInfoResponse.getClientId());
        assertEquals(name, getClientInfoResponse.getName());
        assertEquals(accountId, getClientInfoResponse.getAccounts().get(0).getId());
        assertEquals(accountBalance, getClientInfoResponse.getAccounts().get(0).getBalance());
    }

    @Test
    void getStatementsTest() throws InterruptedException {
        String token = "token";
        String accountId = "1";
        Long from = 1L;
        Long to = 10L;
        String statementId = "100";
        Long amount = 10000L;

        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        [{
                            "id": "%s",
                            "amount": %s
                        }]
                        """.formatted(statementId, amount))
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value()));

        List<StatementResponse> statements = monobankClient.getStatements(token, accountId, from, to);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        assertEquals(String.join("/", getStatementsPath, accountId, from.toString(), to.toString()), recordedRequest.getPath());
        assertEquals(token, recordedRequest.getHeader(tokenHeaderName));

        assertEquals(1, statements.size());
        assertEquals(statementId, statements.get(0).getId());
        assertEquals(amount, statements.get(0).getAmount());
    }
}
