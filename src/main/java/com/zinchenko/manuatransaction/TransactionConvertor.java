package com.zinchenko.manuatransaction;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.manualwallet.domain.Wallet;
import com.zinchenko.manuatransaction.dto.TransactionDto;
import com.zinchenko.manuatransaction.domain.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransactionConvertor {

    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto()
                .setId(transaction.getTransactionId())
                .setAmountInUnits(transaction.getAmountInCents().doubleValue() / 100)
                .setCategoryId(transaction.getCategory().getCategoryId())
                .setDescription(transaction.getDescription())
                .setCreatedAt(transaction.getCreatedAt())
                .setWalletId(transaction.getWallet().getWalletId());
    }

    public Transaction fromDto(TransactionDto transactionDto, Category category, Wallet wallet) {
        return new Transaction()
                .setCreatedAt(Instant.now())
                .setAmountInCents(Double.valueOf(transactionDto.getAmountInUnits() * 100).longValue())
                .setDescription(transactionDto.getDescription())
                .setCategory(category)
                .setWallet(wallet);
    }
}
