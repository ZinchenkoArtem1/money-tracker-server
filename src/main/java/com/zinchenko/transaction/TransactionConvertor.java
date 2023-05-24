package com.zinchenko.transaction;

import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionConvertor {

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
                .setWalletId(transaction.getWallet().getWalletId())
                .setCurrencyName(transaction.getWallet().getCurrency().getName())
                .setWalletType(transaction.getWallet().getWalletType());
    }
}
