package com.zinchenko.transaction;

import com.zinchenko.RandomGenerator;
import com.zinchenko.currencyrate.CurrencyRateHolder;
import com.zinchenko.currencyrate.dto.CurrencyRate;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.user.UserService;
import com.zinchenko.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends RandomGenerator {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionConvertor transactionConvertor;
    @Mock
    private UserService userService;
    @Mock
    private CurrencyRateHolder currencyRateHolder;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, transactionConvertor, userService, currencyRateHolder);
    }

    @Test
    void findAllTest() {
        Transaction transaction = random(Transaction.class);
        TransactionDto transactionDto = random(TransactionDto.class);
        User user = random(User.class);

        when(userService.getActiveUser()).thenReturn(user);
        when(transactionConvertor.toDto(transaction)).thenReturn(transactionDto);
        when(transactionRepository.findAllByUserId(user.getUserId())).thenReturn(List.of(transaction));

        Assertions.assertIterableEquals(List.of(transactionDto), transactionService.findAll());
    }

    @Test
    void findAllByWalletTest() {
        Integer id = random(Integer.class);
        Transaction transaction = random(Transaction.class);
        TransactionDto transactionDto = random(TransactionDto.class);

        when(transactionConvertor.toDto(transaction)).thenReturn(transactionDto);
        when(transactionRepository.findAllByWalletId(id)).thenReturn(List.of(transaction));

        Assertions.assertIterableEquals(List.of(transactionDto), transactionService.findAllByWallet(id));
    }

    @Test
    void getTransactionDtoSuccessTest() {
        Integer id = random(Integer.class);
        Transaction transaction = random(Transaction.class);
        TransactionDto transactionDto = random(TransactionDto.class);

        when(transactionConvertor.toDto(transaction)).thenReturn(transactionDto);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        assertEquals(transactionDto, transactionService.getTransactionDto(id));
    }

    @Test
    void getTransactionDtoFailedTest() {
        Integer id = random(Integer.class);

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> transactionService.getTransactionDto(id));
        assertEquals("Transaction with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void saveAllTest() {
        Transaction transaction = random(Transaction.class);
        CurrencyRate currencyRate = random(CurrencyRate.class);

        when(currencyRateHolder.getCurrencyRate(transaction.getWallet().getCurrency())).thenReturn(currencyRate);

        transactionService.saveAll(List.of(transaction));

        assertEquals(
                BigDecimal.valueOf(currencyRate.getRateBuy() * transaction.getAmountInCents().doubleValue()).longValue(),
                transaction.getAmountInCentsUah()
        );
        verify(transactionRepository).saveAll(List.of(transaction));
    }

    @Test
    void saveTest() {
        Transaction transaction = random(Transaction.class);
        CurrencyRate currencyRate = random(CurrencyRate.class);

        when(currencyRateHolder.getCurrencyRate(transaction.getWallet().getCurrency())).thenReturn(currencyRate);

        transactionService.save(transaction);

        assertEquals(
                BigDecimal.valueOf(currencyRate.getRateBuy() * transaction.getAmountInCents().doubleValue()).longValue(),
                transaction.getAmountInCentsUah()
        );
        verify(transactionRepository).save(transaction);
    }

    @Test
    void deleteTest() {
        Integer id = random(Integer.class);

        transactionService.delete(id);

        verify(transactionRepository).deleteById(id);
    }
}
