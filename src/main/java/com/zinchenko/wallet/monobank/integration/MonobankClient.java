package com.zinchenko.wallet.monobank.integration;

import com.zinchenko.wallet.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.wallet.monobank.integration.dto.StatementResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
//ToDo: use base url
public class MonobankClient {

    private final WebClient webClient;

    @Value("${monobank.api.url}")
    private String monobankApiUrl;
    @Value("${monobank.api.get_client_info_path}")
    private String getClientInfoPath;
    @Value("${monobank.api.get_statements_path}")
    private String getStatementsPath;
    @Value("${monobank.api.token_header_name}")
    private String tokenHeaderName;

    public MonobankClient() {
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
}
