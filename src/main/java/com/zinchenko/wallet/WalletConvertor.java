package com.zinchenko.wallet;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.user.domain.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.stereotype.Component;

@Component
public class WalletConvertor {

    private final MoneyConvertor moneyConvertor;

    public WalletConvertor(MoneyConvertor moneyConvertor) {
        this.moneyConvertor = moneyConvertor;
    }

    public WalletDto toWalletDto(Wallet wallet) {
        return new WalletDto()
                .setId(wallet.getWalletId())
                .setActualBalanceInUnits(moneyConvertor.toUnits(wallet.getActualBalanceInCents()))
                .setName(wallet.getName())
                .setCurrencyId(wallet.getCurrency().getCurrencyId())
                .setCurrencyCode(wallet.getCurrency().getCode())
                .setCurrencyName(wallet.getCurrency().getName())
                .setWalletType(wallet.getWalletType());
    }

    public Wallet toWallet(String name, Currency currency, User user, Double actualBalanceInUnits, WalletType walletType) {
        return new Wallet()
                .setName(name)
                .setActualBalanceInCents(moneyConvertor.toCents(actualBalanceInUnits))
                .setCurrency(currency)
                .setUser(user)
                .setWalletType(walletType);
    }

    public Wallet toWallet(String name, Currency currency, User user, Long actualBalanceInCents, WalletType walletType) {
        return toWallet(name, currency, user, moneyConvertor.toUnits(actualBalanceInCents), walletType);
    }
}
