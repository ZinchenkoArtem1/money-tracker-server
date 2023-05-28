package com.zinchenko.monobank.integration;

import com.zinchenko.monobank.integration.dto.CurrencyRate;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MonobankClient {

    private final WebClient webClient;

    private final String monobankApiUrl;
    private final String getClientInfoPath;
    private final String getStatementsPath;
    private final String tokenHeaderName;

    public MonobankClient(@Value("${monobank.api.url}") String monobankApiUrl,
                          @Value("${monobank.api.get_client_info_path}") String getClientInfoPath,
                          @Value("${monobank.api.get_statements_path}") String getStatementsPath,
                          @Value("${monobank.api.token_header_name}") String tokenHeaderName) {
        this.monobankApiUrl = monobankApiUrl;
        this.getClientInfoPath = getClientInfoPath;
        this.getStatementsPath = getStatementsPath;
        this.tokenHeaderName = tokenHeaderName;
        webClient = WebClient
                .builder()
                .baseUrl(monobankApiUrl)
                .build();
    }

    public GetClientInfoResponse getClientInfo(String token) {
        return webClient.get()
                .uri(monobankApiUrl + getClientInfoPath)
                .header(tokenHeaderName, token)
                .retrieve()
                .bodyToMono(GetClientInfoResponse.class)
                .block();
    }

    public List<StatementResponse> getStatements(String token, String accountId, Long from, Long to) {
        return webClient.get()
                .uri(monobankApiUrl + String.join(
                                "/",
                                getStatementsPath,
                                accountId,
                                from.toString(),
                                to.toString()
                        )
                )
                .header(tokenHeaderName, token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatementResponse>>() {
                })
                .block();
    }

    public List<CurrencyRate> getCurrencyRates() {
        return webClient.get()
                .uri(monobankApiUrl + "/bank/currency")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CurrencyRate>>() {
                })
                .block();
    }
}
