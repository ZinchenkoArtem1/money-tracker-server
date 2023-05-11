package com.zinchenko.wallet;

import com.zinchenko.admin.currency.Currency;
import com.zinchenko.security.model.User;
import org.springframework.stereotype.Component;

@Component
public class WalletConvertor {

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto()
                .setId(wallet.getWalletId())
                .setActualBalance(wallet.getActualBalance())
                .setInitialBalance(wallet.getInitialBalance())
                .setName(wallet.getName())
                .setCurrencyId(wallet.getCurrency().getCurrencyId());
    }

    public Wallet fromDto(WalletDto walletDto, Currency currency, User user) {
        return new Wallet()
                .setWalletId(walletDto.getId())
                .setName(walletDto.getName())
                .setActualBalance(walletDto.getActualBalance())
                .setInitialBalance(walletDto.getInitialBalance())
                .setCurrency(currency)
                .setUser(user);
    }
}
