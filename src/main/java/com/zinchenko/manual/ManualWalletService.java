package com.zinchenko.manual;

import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.springframework.stereotype.Service;

@Service
public class ManualWalletService {

    private final WalletService walletService;
    private final CurrencyService currencyService;

    public ManualWalletService(WalletService walletService, CurrencyService currencyService) {
        this.walletService = walletService;
        this.currencyService = currencyService;
    }

    public void saveManualWallet(CreateWalletRequest request) {
        walletService.save(
                request.getName(), currencyService.getCurrencyById(request.getCurrencyId()),
                request.getActualBalanceInUnits(), WalletType.MANUAL
        );
    }
}
