package com.zinchenko.monobank;

import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MonobankWalletService {

    private final WalletService walletService;
    private final MonobankService monobankService;
    private final CurrencyService currencyService;

    public MonobankWalletService(WalletService walletService, MonobankService monobankService,
                                 CurrencyService currencyService) {
        this.walletService = walletService;
        this.monobankService = monobankService;
        this.currencyService = currencyService;
    }

    public void createMonobankWallet(CreateWalletRequest request) {
        AccountResponse accountResponse = monobankService.getAccount(request);
        Currency currency = currencyService.getCurrencyByCode(accountResponse.getCurrencyCode());
        Long balance = accountResponse.getBalance();
        Instant to = Instant.now();

        Wallet wallet = walletService.save(
                request.getName(), currency, balance, request.getWalletType()
        );

        monobankService.createMonobankData(request, wallet, to);
        monobankService.updateTransactions(request, to, wallet);
    }
}
