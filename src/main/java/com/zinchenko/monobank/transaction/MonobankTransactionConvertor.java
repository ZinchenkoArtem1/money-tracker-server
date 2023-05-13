package com.zinchenko.monobank.transaction;

import com.zinchenko.monobank.transaction.domain.MonobankTransaction;
import com.zinchenko.monobank.transaction.dto.MonobankTransactionDto;
import org.springframework.stereotype.Component;

@Component
public class MonobankTransactionConvertor {

    public MonobankTransactionDto toDto(MonobankTransaction monobankTransaction) {
        return new MonobankTransactionDto()
                .setId(monobankTransaction.getMonobankTransactionId())
                .setAmountInUnits(Double.valueOf(monobankTransaction.getAmountInCents()) / 100)
                .setDescription(monobankTransaction.getDescription())
                .setCategoryId(monobankTransaction.getCategory().getCategoryId())
                .setCreatedAt(monobankTransaction.getCreatedAt())
                .setMonobankWalletId(monobankTransaction.getMonobankWallet().getMonobankWalletId());
    }
}
