package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConvertor {

    public CurrencyDto toDto(Currency currency) {
        return new CurrencyDto()
                .setId(currency.getCurrencyId())
                .setName(currency.getName())
                .setCode(currency.getCode());
    }

    public Currency fromDto(CurrencyDto currencyDto) {
        return new Currency()
                .setCurrencyId(currencyDto.getId())
                .setName(currencyDto.getName())
                .setCode(currencyDto.getCode());
    }
}
