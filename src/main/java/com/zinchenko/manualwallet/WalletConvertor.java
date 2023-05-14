package com.zinchenko.manualwallet;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.manualwallet.domain.Wallet;
import com.zinchenko.manualwallet.dto.WalletDto;
import com.zinchenko.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class WalletConvertor {

    private final MoneyConvertor moneyConvertor;

    public WalletConvertor(MoneyConvertor moneyConvertor) {
        this.moneyConvertor = moneyConvertor;
    }

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto()
                .setId(wallet.getWalletId())
                .setActualBalanceInUnits(moneyConvertor.toUnits(wallet.getActualBalanceInCents()))
                .setInitialBalanceInUnits(moneyConvertor.toUnits(wallet.getInitialBalanceInCents()))
                .setName(wallet.getName())
                .setCurrencyId(wallet.getCurrency().getCurrencyId())
                .setCurrencyCode(wallet.getCurrency().getCode())
                .setCurrencyName(wallet.getCurrency().getName());
    }

    public Wallet fromDto(WalletDto walletDto, Currency currency, User user) {
        return new Wallet()
                .setName(walletDto.getName())
                .setInitialBalanceInCents(moneyConvertor.toCents(walletDto.getInitialBalanceInUnits()))
                .setActualBalanceInCents(moneyConvertor.toCents(walletDto.getInitialBalanceInUnits()))
                .setCurrency(currency)
                .setUser(user);
    }
}
