package com.zinchenko.manual;

import com.zinchenko.AbstractTest;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManualWalletServiceTest extends AbstractTest {

    @Mock
    private WalletService walletService;
    @Mock
    private CurrencyService currencyService;

    private ManualWalletService manualWalletService;

    @BeforeEach
    void setUp() {
        manualWalletService = new ManualWalletService(walletService, currencyService);
    }

    @Test
    void saveManualWalletTest() {
        CreateWalletRequest request = random(CreateWalletRequest.class);
        Currency currency = random(Currency.class);

        when(currencyService.getCurrency(request.getCurrencyId())).thenReturn(currency);
        manualWalletService.saveManualWallet(request);

        verify(walletService).save(request.getName(), currency, request.getActualBalanceInUnits(), WalletType.MANUAL);
    }
}
