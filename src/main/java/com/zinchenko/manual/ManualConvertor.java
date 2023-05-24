package com.zinchenko.manual;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ManualConvertor {

    private final MoneyConvertor moneyConvertor;

    public ManualConvertor(MoneyConvertor moneyConvertor) {
        this.moneyConvertor =  moneyConvertor;
    }

    public Transaction toManualTransaction(TransactionDto transactionDto, Category category, Wallet wallet) {
        return new Transaction()
                .setCreatedAt(Instant.now())
                .setAmountInCents(moneyConvertor.toCents(transactionDto.getAmountInUnits()))
                .setDescription(transactionDto.getDescription())
                .setCategory(category)
                .setWallet(wallet);
    }
}
