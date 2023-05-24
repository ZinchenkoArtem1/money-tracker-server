package com.zinchenko.manual;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ManualConvertorTest extends RandomGenerator {

    private ManualConvertor manualConvertor;

    @BeforeEach
    void setUp() {
        manualConvertor = new ManualConvertor(new MoneyConvertor());
    }

    @Test
    void toManualTransactionTest() {
        TransactionDto transactionDto = random(TransactionDto.class)
                .setAmountInUnits(random(Long.class).doubleValue());
        Category category = random(Category.class);
        Wallet wallet = random(Wallet.class);

        Transaction transaction = manualConvertor.toManualTransaction(transactionDto, category, wallet);

        assertNotNull(transaction.getCreatedAt());
        assertEquals(transactionDto.getAmountInUnits().longValue() * 100, transaction.getAmountInCents());
        assertEquals(category, transaction.getCategory());
        assertEquals(wallet, transaction.getWallet());
    }
}
