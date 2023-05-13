package com.zinchenko.wallet.monobank;

import com.zinchenko.admin.category.Category;
import com.zinchenko.admin.currency.Currency;
import com.zinchenko.user.model.User;
import com.zinchenko.wallet.monobank.domain.MonobankTransaction;
import com.zinchenko.wallet.monobank.domain.MonobankWallet;
import com.zinchenko.wallet.monobank.dto.CreateMonobankWalletRequest;
import com.zinchenko.wallet.monobank.integration.dto.StatementResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MonobankWalletConvertor {

    public MonobankWallet toMonobankWallet(CreateMonobankWalletRequest request, Currency currency,
                                           Long actualBalance, User user, Instant to) {
        return new MonobankWallet()
                .setName(request.getName())
                .setAccountId(request.getAccountId())
                .setToken(request.getToken())
                .setCurrency(currency)
                .setActualBalanceInCents(actualBalance)
                .setUser(user)
                .setLastSyncDate(to);
    }

    public MonobankTransaction toMonobankTransaction(StatementResponse statementResponse, MonobankWallet monobankWallet) {
        return new MonobankTransaction()
                .setCreatedAt(Instant.ofEpochSecond(statementResponse.getTime()))
                .setDescription(statementResponse.getDescription())
                .setMonobankWallet(monobankWallet)
                .setAmountInCents(statementResponse.getAmount())
                //ToDo: map category from monobank to internal categories
                .setCategory(new Category()
                        .setCategoryId(1)
                        .setName("default"));
    }
}
