package com.zinchenko.wallet;

import com.zinchenko.admin.currency.Currency;
import com.zinchenko.user.model.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.stereotype.Component;

@Component
public class WalletConvertor {

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto()
                .setId(wallet.getWalletId())
                .setActualBalanceInUnits(wallet.getActualBalanceInCents().doubleValue() / 100)
                .setInitialBalanceInUnits(wallet.getInitialBalanceInCents().doubleValue() / 100)
                .setName(wallet.getName())
                .setCurrencyId(wallet.getCurrency().getCurrencyId());
    }

    public Wallet fromDto(WalletDto walletDto, Currency currency, User user) {
        return new Wallet()
                .setName(walletDto.getName())
                .setInitialBalanceInCents(Double.valueOf(walletDto.getInitialBalanceInUnits() * 100).longValue())
                .setActualBalanceInCents(Double.valueOf(walletDto.getInitialBalanceInUnits() * 100).longValue())
                .setCurrency(currency)
                .setUser(user);
    }
}
