package com.zinchenko.transaction;

import com.zinchenko.AbstractTest;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.transaction.dto.TransactionDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends AbstractTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionConvertor transactionConvertor;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, transactionConvertor);
    }

    @Test
    void findAllTest() {
        Transaction transaction = random(Transaction.class);
        TransactionDto transactionDto = random(TransactionDto.class);

        when(transactionConvertor.toDto(transaction)).thenReturn(transactionDto);
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

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
    void checkExistSuccessTest() {
        Integer id = random(Integer.class);

        when(transactionRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> transactionService.checkExist(id));
    }

    @Test
    void checkExistFailedTest() {
        Integer id = random(Integer.class);

        when(transactionRepository.existsById(id)).thenReturn(false);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> transactionService.checkExist(id));
        assertEquals("Transaction with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void saveAllTest() {
        Transaction transaction = random(Transaction.class);

        transactionService.saveAll(List.of(transaction));

        verify(transactionRepository).saveAll(List.of(transaction));
    }

    @Test
    void saveTest() {
        Transaction transaction = random(Transaction.class);

        transactionService.save(transaction);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void deleteTest() {
        Integer id = random(Integer.class);

        transactionService.delete(id);

        verify(transactionRepository).deleteById(id);
    }
}
