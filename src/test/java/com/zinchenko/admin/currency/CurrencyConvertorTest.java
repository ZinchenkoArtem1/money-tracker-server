package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrencyConvertorTest {

    private CurrencyConvertor currencyConvertor;

    @BeforeEach
    void setUp() {
        currencyConvertor = new CurrencyConvertor();
    }

    @Test
    void toDtoTest() {
        Currency currency = new Currency()
                .setCurrencyId(RandomUtils.nextInt())
                .setName(UUID.randomUUID().toString())
                .setCode(RandomUtils.nextInt());

        CurrencyDto currencyDto = currencyConvertor.toDto(currency);

        assertEquals(currency.getCurrencyId(), currencyDto.getId());
        assertEquals(currency.getName(), currencyDto.getName());
        assertEquals(currency.getCode(), currencyDto.getCode());
    }

    @Test
    void fromDtoTest() {
        CurrencyDto currencyDto = new CurrencyDto()
                .setId(RandomUtils.nextInt())
                .setName(UUID.randomUUID().toString())
                .setCode(RandomUtils.nextInt());

        Currency currency = currencyConvertor.fromDto(currencyDto);

        assertEquals(currencyDto.getId(), currency.getCurrencyId());
        assertEquals(currencyDto.getName(), currency.getName());
        assertEquals(currencyDto.getName(), currency.getName());
    }
}
