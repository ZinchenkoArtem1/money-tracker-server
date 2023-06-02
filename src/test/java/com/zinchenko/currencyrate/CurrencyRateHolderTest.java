package com.zinchenko.currencyrate;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.currencyrate.dto.CurrencyRate;
import com.zinchenko.monobank.integration.MonobankClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateHolderTest extends RandomGenerator {

    private CurrencyRateHolder currencyRateHolder;

    @Mock
    private CurrencyRateClient currencyRateClient;

    private CurrencyRate currencyRate;

    @BeforeEach
    void setUp() {
        currencyRate = random(CurrencyRate.class);

        when(currencyRateClient.getCurrencyRates()).thenReturn(List.of(currencyRate));
        currencyRateHolder = new CurrencyRateHolder(currencyRateClient);
    }

    @Test
    void getCurrencyRateTest() {
        Currency currency = random(Currency.class)
                .setCode(currencyRate.getCurrencyCodeA());

        assertEquals(currencyRate, currencyRateHolder.getCurrencyRate(currency));
    }

    @Test
    void getCurrencyRateUahTest() {
        Currency currency = random(Currency.class)
                .setCode(980);

        assertEquals(1D, currencyRateHolder.getCurrencyRate(currency).getRateBuy());
    }
}
