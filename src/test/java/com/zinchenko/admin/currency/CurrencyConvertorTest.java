package com.zinchenko.admin.currency;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrencyConvertorTest extends RandomGenerator {

    private CurrencyConvertor currencyConvertor;

    @BeforeEach
    void setUp() {
        currencyConvertor = new CurrencyConvertor();
    }

    @Test
    void toDtoTest() {
        Currency currency = random(Currency.class);

        CurrencyDto currencyDto = currencyConvertor.toDto(currency);

        assertEquals(currency.getCurrencyId(), currencyDto.getId());
        assertEquals(currency.getNameEng(), currencyDto.getName());
        assertEquals(currency.getCode(), currencyDto.getCode());
    }
}
