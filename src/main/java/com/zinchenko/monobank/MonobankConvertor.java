package com.zinchenko.monobank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MonobankConvertor {

    private final CategoryService categoryService;

    public MonobankConvertor(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ClientAccountResponse toClientAccountResponse(AccountResponse accountResponse) {
        return new ClientAccountResponse()
                .setId(accountResponse.getId())
                .setMaskedPan(accountResponse.getMaskedPan().get(0));
    }

    public MonobankData toMonobankData(Wallet wallet, Instant syncDate, String token, String accountId) {
        return new MonobankData()
                .setWallet(wallet)
                .setLastSyncDate(syncDate)
                .setToken(token)
                .setAccountId(accountId);
    }

    public Transaction fromMonobankTransaction(StatementResponse statementResponse, Wallet wallet) {
        return new Transaction()
                .setCreatedAt(Instant.ofEpochSecond(statementResponse.getTime()))
                .setDescription(statementResponse.getDescription())
                .setWallet(wallet)
                .setAmountInCents(statementResponse.getAmount())
                .setCategory(mapCategory(statementResponse.getMcc()));
    }

    private Category mapCategory(Integer mcc) {
        return categoryService.getCategoryByMonobankMcc(mcc);
    }
}
