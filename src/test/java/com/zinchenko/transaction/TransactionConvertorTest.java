package com.zinchenko.transaction;

import com.zinchenko.RandomGenerator;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionConvertorTest extends RandomGenerator {

    private TransactionConvertor transactionConvertor;

    @BeforeEach
    void setUp() {
        transactionConvertor = new TransactionConvertor(new MoneyConvertor());
    }

    @Test
    void toDto() {
        Transaction transaction = random(Transaction.class);

        TransactionDto transactionDto = transactionConvertor.toDto(transaction);

        assertEquals(transaction.getTransactionId(), transactionDto.getId());
        assertEquals(transaction.getAmountInCents().doubleValue() / 100, transactionDto.getAmountInUnits());
        assertEquals(transaction.getCategory().getCategoryId(), transactionDto.getCategoryId());
        assertEquals(transaction.getCategory().getName(), transactionDto.getCategoryName());
        assertEquals(transaction.getWallet().getCurrency().getName(), transactionDto.getCurrencyName());
        assertEquals(transaction.getWallet().getWalletId(), transactionDto.getWalletId());
        assertEquals(transaction.getDescription(), transactionDto.getDescription());
        assertEquals(transaction.getWallet().getWalletType(), transactionDto.getWalletType());
    }
}
