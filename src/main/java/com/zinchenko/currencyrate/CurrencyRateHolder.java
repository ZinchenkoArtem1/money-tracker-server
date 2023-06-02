package com.zinchenko.currencyrate;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.currencyrate.dto.CurrencyRate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class CurrencyRateHolder {

    private static final Integer UAH_CURRENCY_CODE = 980;
    // Monobank API provide rates for currencies relatively UAH
    // Monobank API don't provide rate UAH relatively UAH
    // When we need get rate for UAH we return this default rate object
    private static final CurrencyRate DEFAULT_CURRENCY_RATE_FOR_UAH = new CurrencyRate()
            .setCurrencyCodeA(UAH_CURRENCY_CODE)
            .setCurrencyCodeB(UAH_CURRENCY_CODE)
            .setRateBuy(1D)
            .setRateSell(1D);
    private final CurrencyRateClient currencyRateClient;
    private List<CurrencyRate> currencyRates;

    public CurrencyRateHolder(CurrencyRateClient currencyRateClient) {
        this.currencyRateClient = currencyRateClient;
        currencyRates = currencyRateClient.getCurrencyRates();
    }

    // Monobank API update currency rates every 5 minutes
    // This job that fetch updated currencies rate from Monobank API and save in memory
    @Scheduled(initialDelay = 5, fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    private void updateRates() {
        currencyRates = currencyRateClient.getCurrencyRates();
    }

    public CurrencyRate getCurrencyRate(Currency currency) {
        if (Objects.equals(currency.getCode(), UAH_CURRENCY_CODE)) {
            return DEFAULT_CURRENCY_RATE_FOR_UAH;
        } else {
            return currencyRates.stream()
                    .filter(cr -> cr.getCurrencyCodeA().equals(currency.getCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Rate for currency with code [%s] not found".formatted(currency.getCode())));
        }
    }
}
