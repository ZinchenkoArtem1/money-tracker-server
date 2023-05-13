package com.zinchenko.monobank.wallet;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.monobank.wallet.dto.MonobankWalletDto;
import com.zinchenko.monobank.wallet.domain.MonobankWallet;
import com.zinchenko.user.model.User;
import com.zinchenko.monobank.transaction.domain.MonobankTransaction;
import com.zinchenko.monobank.wallet.dto.CreateMonobankWalletRequest;
import com.zinchenko.monobank.integration.dto.StatementResponse;
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

    public MonobankWalletDto toDto(MonobankWallet monobankWallet) {
        return new MonobankWalletDto()
                .setId(monobankWallet.getMonobankWalletId())
                .setName(monobankWallet.getName())
                .setCurrencyId(monobankWallet.getCurrency().getCurrencyId())
                .setActualBalanceInUnits(Double.valueOf(monobankWallet.getActualBalanceInCents()) / 100)
                .setLastSyncDate(monobankWallet.getLastSyncDate());
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
