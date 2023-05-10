package com.zinchenko.admin.currency;

import org.springframework.stereotype.Service;

@Service
public class CurrencyConvertor {

    public CurrencyDto toDto(Currency currency) {
        return new CurrencyDto()
                .setId(currency.getCurrencyId())
                .setName(currency.getName());
    }

    public Currency fromDto(CurrencyDto currencyDto) {
        return new Currency()
                .setName(currencyDto.getName());
    }
}
