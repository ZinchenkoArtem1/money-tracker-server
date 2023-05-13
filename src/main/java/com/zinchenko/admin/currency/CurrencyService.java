package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.domain.CurrencyRepository;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return currencyConvertor.toDto(getCurrency(id));
    }

    public Currency getCurrency(Integer id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Currency with id [%s] not found".formatted(id))
                );
    }

    public Currency getCurrencyByCode(Integer code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Currency with code [%s] not found".formatted(code))
                );
    }

    public void create(CurrencyDto currencyDto) {
        if (currencyDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create currency operation");
        } else {
            currencyRepository.save(currencyConvertor.fromDto(currencyDto));
        }
    }

    public void update(CurrencyDto currencyDto) {
        checkExist(currencyDto.getId());
        currencyRepository.save(currencyConvertor.fromDto(currencyDto));
    }

    public void deleteById(Integer id) {
        checkExist(id);
        currencyRepository.deleteById(id);
    }

    private void checkExist(Integer id) {
        if (!currencyRepository.existsById(id)) {
            throw new IllegalStateException("Currency with id [%s] not found".formatted(id));
        }
    }
}
