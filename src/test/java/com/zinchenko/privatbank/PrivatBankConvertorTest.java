package com.zinchenko.privatbank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivatBankConvertorTest extends RandomGenerator {

    private static final String CATEGORY_PRIVATBANK = "category";
    private static final String DESCRIPTION = "description";
    private static final String CURRENCY_PRIVATBANK = "грн";
    private static final String BALANCE_UNITS = "150.15";
    private static final Long BALANCE_CENTS = 15015L;
    private static final String AMOUNT_UNITS = "-1.00";
    private static final Long AMOUNT_CENTS = -100L;
    private final Map<Integer, List<String>> data = new LinkedHashMap<>();
    private PrivatBankConvertor privatBankConvertor;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        privatBankConvertor = new PrivatBankConvertor(categoryService, currencyService, new MoneyConvertor());
        data.put(0, List.of("title"));
        data.put(1, List.of("title"));
        data.put(2, List.of("28.05.2023", "18:28", CATEGORY_PRIVATBANK, "", DESCRIPTION, AMOUNT_UNITS, CURRENCY_PRIVATBANK, "", "", BALANCE_UNITS));
        data.put(3, List.of("footer"));
    }

    @Test
    void getBalanceTest() {
        assertEquals(BALANCE_CENTS, privatBankConvertor.getBalance(data));
    }

    @Test
    void getCurrencyTest() {
        Currency currency = random(Currency.class);
        when(currencyService.getCurrencyByNameUkr(CURRENCY_PRIVATBANK)).thenReturn(currency);

        assertEquals(currency, privatBankConvertor.getCurrency(data));
    }

    @Test
    void convertTransactionsTest() {
        Wallet wallet = random(Wallet.class);
        Category category = random(Category.class);

        when(categoryService.getCategoryByPrivatBankCategoryName(CATEGORY_PRIVATBANK)).thenReturn(category);

        List<Transaction> transactions = privatBankConvertor.convertTransactions(data, wallet);

        assertEquals(DESCRIPTION, transactions.get(0).getDescription());
        assertEquals(AMOUNT_CENTS, transactions.get(0).getAmountInCents());
        assertEquals(wallet, transactions.get(0).getWallet());
        assertEquals(category, transactions.get(0).getCategory());
    }
}
