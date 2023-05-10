package com.zinchenko.admin.currency;

import com.zinchenko.admin.category.CategoryDto;
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

    public CurrencyDto getById(Integer id) {
        return currencyConvertor.toDto(currencyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Currency with id [%s] not found".formatted(id)))
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
        if (!currencyRepository.existsById(currencyDto.getId())) {
            throw new IllegalStateException("Currency with id [%s] not found".formatted(currencyDto.getId()));
        } else {
            currencyRepository.save(currencyConvertor.fromDto(currencyDto));
        }
    }

    public void deleteById(Integer id) {
        currencyRepository.deleteById(id);
    }
}
