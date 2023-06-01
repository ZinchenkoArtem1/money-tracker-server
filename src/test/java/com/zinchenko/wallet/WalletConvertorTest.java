package com.zinchenko.wallet;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.user.domain.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.WalletDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WalletConvertorTest {

    private static final Integer ID = 1;
    private static final Long BALANCE_CENTS = 100L;
    private static final Double BALANCE_UNITS = 1.0;
    private static final String NAME = "name";
    private static final Integer CURRENCY_ID = 2;
    private static final String CURRENCY_NAME = "USD";
    private static final Integer CURRENCY_CODE = 900;
    private static final Currency CURRENCY = new Currency()
            .setCurrencyId(CURRENCY_ID)
            .setCode(CURRENCY_CODE)
            .setNameEng(CURRENCY_NAME);
    private WalletConvertor walletConvertor;

    @BeforeEach
    void setUp() {
        walletConvertor = new WalletConvertor(new MoneyConvertor());
    }

    @Test
    void toWalletDtoTest() {
        Wallet wallet = new Wallet()
                .setWalletId(ID)
                .setActualBalanceInCents(BALANCE_CENTS)
                .setName(NAME)
                .setCurrency(CURRENCY)
                .setWalletType(WalletType.MANUAL);

        WalletDto walletDto = walletConvertor.toWalletDto(wallet);

        assertEquals(WalletType.MANUAL, walletDto.getWalletType());
        assertEquals(BALANCE_UNITS, walletDto.getActualBalanceInUnits());
        assertEquals(CURRENCY_CODE, walletDto.getCurrencyCode());
        assertEquals(CURRENCY_ID, walletDto.getCurrencyId());
        assertEquals(CURRENCY_NAME, walletDto.getCurrencyName());
        assertEquals(NAME, walletDto.getName());
        assertEquals(ID, walletDto.getId());
    }

    @Test
    void toWalletBalanceInUnits() {
        User user = mock(User.class);

        Wallet wallet = walletConvertor.toWallet(NAME, CURRENCY, user, BALANCE_UNITS, WalletType.MONOBANK);

        assertEquals(BALANCE_CENTS, wallet.getActualBalanceInCents());
        assertEquals(NAME, wallet.getName());
        assertEquals(CURRENCY, wallet.getCurrency());
        assertEquals(user, wallet.getUser());
        assertEquals(WalletType.MONOBANK, wallet.getWalletType());
    }

    @Test
    void toWalletBalanceInCents() {
        User user = mock(User.class);

        Wallet wallet = walletConvertor.toWallet(NAME, CURRENCY, user, BALANCE_CENTS, WalletType.MONOBANK);

        assertEquals(BALANCE_CENTS, wallet.getActualBalanceInCents());
        assertEquals(NAME, wallet.getName());
        assertEquals(CURRENCY, wallet.getCurrency());
        assertEquals(user, wallet.getUser());
        assertEquals(WalletType.MONOBANK, wallet.getWalletType());
    }
}
