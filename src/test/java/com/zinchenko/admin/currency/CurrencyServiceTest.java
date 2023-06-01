package com.zinchenko.admin.currency;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.domain.CurrencyRepository;
import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest extends RandomGenerator {

    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CurrencyConvertor currencyConvertor;

    @BeforeEach
    void setUp() {
        this.currencyService = new CurrencyService(currencyRepository, currencyConvertor);
    }

    @Test
    void findAllNotEmptyTest() {
        Currency currency1 = random(Currency.class);
        Currency currency2 = random(Currency.class);
        CurrencyDto currencyDto1 = random(CurrencyDto.class);
        CurrencyDto currencyDto2 = random(CurrencyDto.class);

        when(currencyRepository.findAll()).thenReturn(List.of(currency1, currency2));
        when(currencyConvertor.toDto(currency1)).thenReturn(currencyDto1);
        when(currencyConvertor.toDto(currency2)).thenReturn(currencyDto2);

        List<CurrencyDto> currenciesDto = currencyService.findAll();

        Assertions.assertEquals(2, currenciesDto.size());
        Assertions.assertIterableEquals(List.of(currencyDto1, currencyDto2), currenciesDto);
    }

    @Test
    void findAllEmptyTest() {
        when(currencyRepository.findAll()).thenReturn(List.of());
        verifyNoInteractions(currencyConvertor);

        Assertions.assertTrue(currencyService.findAll().isEmpty());
    }

    @Test
    void getCurrencyDtoSuccessTest() {
        Integer id = RandomUtils.nextInt();
        Currency currency = random(Currency.class);
        CurrencyDto currencyDto = random(CurrencyDto.class);

        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        when(currencyConvertor.toDto(currency)).thenReturn(currencyDto);

        assertEquals(currencyDto, currencyService.getCurrencyDto(id));
    }

    @Test
    void getCurrencyDtoFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.findById(id)).thenReturn(Optional.empty());
        verifyNoInteractions(currencyConvertor);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.getCurrencyDto(id));
        assertEquals("Currency with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void getCurrencyByIdSuccessTest() {
        Currency currency = random(Currency.class);

        when(currencyRepository.findById(currency.getCurrencyId())).thenReturn(Optional.of(currency));

        assertEquals(currency, currencyService.getCurrencyById(currency.getCurrencyId()));
    }

    @Test
    void getCurrencyByIdFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.findById(id)).thenReturn(Optional.empty());
        verifyNoInteractions(currencyConvertor);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.getCurrencyById(id));
        assertEquals("Currency with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void getCurrencyByCodeSuccessTest() {
        Currency currency = random(Currency.class);

        when(currencyRepository.findByCode(currency.getCode())).thenReturn(Optional.of(currency));

        assertEquals(currency, currencyService.getCurrencyByCode(currency.getCode()));
    }

    @Test
    void getCurrencyByCodeFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.findByCode(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.getCurrencyByCode(id));
        assertEquals("Currency with code [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void getCurrencyByNameUkrSuccessTest() {
        Currency currency = random(Currency.class);

        when(currencyRepository.findByNameUkr(currency.getNameUkr().toUpperCase(Locale.ROOT))).thenReturn(Optional.of(currency));

        assertEquals(currency, currencyService.getCurrencyByNameUkr(currency.getNameUkr()));
    }

    @Test
    void getCurrencyByNameUkrFailedTest() {
        String nameUkr = random(String.class);

        when(currencyRepository.findByNameUkr(nameUkr.toUpperCase(Locale.ROOT))).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.getCurrencyByNameUkr(nameUkr));
        assertEquals("Currency with name ukr [%s] not found".formatted(nameUkr), exc.getMessage());
    }
}
