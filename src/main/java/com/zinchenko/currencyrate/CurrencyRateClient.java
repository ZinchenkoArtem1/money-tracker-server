package com.zinchenko.currencyrate;

import com.zinchenko.currencyrate.dto.CurrencyRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class CurrencyRateClient {

    private final WebClient webClient;
    private final String monobankApiUrl;

    public CurrencyRateClient(@Value("${monobank.api.url}") String monobankApiUrl) {
        this.monobankApiUrl = monobankApiUrl;
        webClient = WebClient
                .builder()
                .baseUrl(monobankApiUrl)
                .build();
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
