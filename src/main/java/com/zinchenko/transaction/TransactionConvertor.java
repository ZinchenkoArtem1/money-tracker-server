package com.zinchenko.transaction;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransactionConvertor {

    private static final Category DEFAULT_CATEGORY = new Category().setCategoryId(1);
    private final MoneyConvertor moneyConvertor;

    public TransactionConvertor(MoneyConvertor moneyConvertor) {
        this.moneyConvertor = moneyConvertor;
    }

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto()
                .setId(transaction.getTransactionId())
                .setAmountInUnits(moneyConvertor.toUnits(transaction.getAmountInCents()))
                .setCategoryId(transaction.getCategory().getCategoryId())
                .setCategoryName(transaction.getCategory().getName())
                .setDescription(transaction.getDescription())
                .setCreatedAt(transaction.getCreatedAt())
                .setWalletId(transaction.getWallet().getWalletId())
                .setCurrencyName(transaction.getWallet().getCurrency().getName());
    }

    public Transaction fromDto(TransactionDto transactionDto, Category category, Wallet wallet) {
        return new Transaction()
                .setCreatedAt(Instant.now())
                .setAmountInCents(moneyConvertor.toCents(transactionDto.getAmountInUnits()))
                .setDescription(transactionDto.getDescription())
                .setCategory(category)
                .setWallet(wallet);
    }

    public Transaction fromMonobankTransaction(StatementResponse statementResponse, Wallet wallet) {
        return new Transaction()
                .setCreatedAt(Instant.ofEpochSecond(statementResponse.getTime()))
                .setDescription(statementResponse.getDescription())
                .setWallet(wallet)
                .setAmountInCents(statementResponse.getAmount())
                .setCategory(DEFAULT_CATEGORY);
    }
}
