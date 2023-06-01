package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.domain.CurrencyRepository;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyConvertor currencyConvertor;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyConvertor currencyConvertor) {
        this.currencyRepository = currencyRepository;
        this.currencyConvertor = currencyConvertor;
    }

    public List<CurrencyDto> findAll() {
        return currencyRepository.findAll().stream()
                .map(currencyConvertor::toDto)
                .toList();
    }

    public CurrencyDto getCurrencyDto(Integer id) {
        return currencyConvertor.toDto(getCurrencyById(id));
    }

    public Currency getCurrencyById(Integer id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Currency with id [%s] not found".formatted(id))
                );
    }

    public Currency getCurrencyByCode(Integer code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Currency with code [%s] not found".formatted(code))
                );
    }

    public Currency getCurrencyByNameUkr(String nameUkr) {
        return currencyRepository.findByNameUkr(nameUkr.toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalStateException("Currency with name ukr [%s] not found".formatted(nameUkr))
                );
    }
}
