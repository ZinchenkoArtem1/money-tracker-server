package com.zinchenko.admin.currency;

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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

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
        Currency currency1 = mock(Currency.class);
        Currency currency2 = mock(Currency.class);
        CurrencyDto currencyDto1 = mock(CurrencyDto.class);
        CurrencyDto currencyDto2 = mock(CurrencyDto.class);

        when(currencyRepository.findAll()).thenReturn(List.of(currency1, currency2));
        when(currencyConvertor.toDto(currency1)).thenReturn(currencyDto1);
        when(currencyConvertor.toDto(currency2)).thenReturn(currencyDto2);

        List<CurrencyDto> currenciesDto = currencyService.findAll();

        Assertions.assertFalse(currenciesDto.isEmpty());
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
        Currency currency = mock(Currency.class);
        CurrencyDto currencyDto = mock(CurrencyDto.class);

        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));
        when(currencyConvertor.toDto(currency)).thenReturn(currencyDto);

        assertEquals(currencyDto, currencyService.getCurrencyDto(id));
    }

    @Test
    void getCurrencyDtoFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.findById(id)).thenReturn(Optional.empty());
        verifyNoInteractions(currencyConvertor);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.getCurrency(id));
        assertEquals("Currency with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void createSuccessTest() {
        Currency currency = mock(Currency.class);
        CurrencyDto currencyDto = mock(CurrencyDto.class);

        when(currencyDto.getId()).thenReturn(null);
        when(currencyConvertor.fromDto(currencyDto)).thenReturn(currency);

        currencyService.create(currencyDto);

        verify(currencyRepository).save(currency);
    }

    @Test
    void createWithIdInRequestTest() {
        CurrencyDto currencyDto = mock(CurrencyDto.class);

        when(currencyDto.getId()).thenReturn(RandomUtils.nextInt());
        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.create(currencyDto));

        assertEquals("Request body must not contain id for the create currency operation", exc.getMessage());
        verifyNoInteractions(currencyConvertor);
    }

    @Test
    void updateExistTest() {
        Integer id = RandomUtils.nextInt();
        String newName = UUID.randomUUID().toString();
        Integer newCode = RandomUtils.nextInt();
        Currency currency = mock(Currency.class);
        CurrencyDto currencyDto = mock(CurrencyDto.class);

        when(currencyDto.getId()).thenReturn(id);
        when(currencyDto.getName()).thenReturn(newName);
        when(currencyDto.getCode()).thenReturn(newCode);
        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));

        currencyService.update(currencyDto);

        verify(currency).setName(newName);
        verify(currency).setCode(newCode);
        verify(currencyRepository).save(currency);
    }

    @Test
    void updateNotExistTest() {
        Integer id = RandomUtils.nextInt();
        CurrencyDto currencyDto = mock(CurrencyDto.class);

        when(currencyDto.getId()).thenReturn(id);
        when(currencyRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.update(currencyDto));
        assertEquals("Currency with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void deleteByIdNotExistTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.existsById(id)).thenReturn(false);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> currencyService.deleteById(id));
        assertEquals("Currency with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void deleteByIdSuccessTest() {
        Integer id = RandomUtils.nextInt();

        when(currencyRepository.existsById(id)).thenReturn(true);

        currencyService.deleteById(id);

        verify(currencyRepository).deleteById(id);
    }
}
