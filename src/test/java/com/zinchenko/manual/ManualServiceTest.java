package com.zinchenko.manual;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.TransactionService;
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
class ManualServiceTest extends RandomGenerator {

    @Mock
    private TransactionService transactionService;
    @Mock
    private WalletService walletService;
    @Mock
    private ManualConvertor manualConvertor;
    @Mock
    private MoneyConvertor moneyConvertor;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CurrencyService currencyService;

    private ManualService manualService;

    @BeforeEach
    void setUp() {
        manualService = new ManualService(transactionService, categoryService,
                walletService, manualConvertor,
                moneyConvertor, currencyService);
    }

    @Test
    void saveManualWalletTest() {
        CreateWalletRequest request = random(CreateWalletRequest.class);
        Currency currency = random(Currency.class);

        when(currencyService.getCurrencyById(request.getCurrencyId())).thenReturn(currency);
        manualService.createWallet(request);

        verify(walletService).save(request.getName(), currency, request.getActualBalanceInUnits(), WalletType.MANUAL);
    }
}
