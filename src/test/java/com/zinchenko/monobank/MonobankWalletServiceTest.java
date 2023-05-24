package com.zinchenko.monobank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonobankWalletServiceTest extends RandomGenerator {

    @Mock
    private WalletService walletService;
    @Mock
    private MonobankService monobankService;
    @Mock
    private CurrencyService currencyService;

    private MonobankWalletService monobankWalletService;

    @BeforeEach
    void setUp() {
        monobankWalletService = new MonobankWalletService(walletService, monobankService, currencyService);
    }

    @Test
    void createMonobankWalletTest() {
        CreateWalletRequest request = random(CreateWalletRequest.class);
        AccountResponse accountResponse = random(AccountResponse.class);
        Currency currency = random(Currency.class);
        Wallet wallet = random(Wallet.class);

        when(monobankService.getAccount(request)).thenReturn(accountResponse);
        when(currencyService.getCurrencyByCode(accountResponse.getCurrencyCode())).thenReturn(currency);
        when(walletService.save(request.getName(), currency, accountResponse.getBalance(), request.getWalletType()))
                .thenReturn(wallet);

        monobankWalletService.createMonobankWallet(request);

        verify(monobankService).createMonobankData(eq(request), eq(wallet), any());
        verify(monobankService).updateTransactions(eq(request), any(), eq(wallet));
    }
}
