package com.zinchenko.privatbank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.wallet.domain.Wallet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class PrivatBankConvertor {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm");
    private static final Integer FIRST_ROW_NUMBER_WITH_DATA_IN_EXCEL_FILE = 2;
    private static final Integer COLUMN_NUMBER_WITH_CURRENCY_IN_EXCEL_FILE = 6;
    private static final Integer COLUMN_NUMBER_WITH_BALANCE_IN_EXCEL_FILE = 9;
    private static final Integer COLUMN_NUMBER_WITH_CATEGORY_IN_EXCEL_FILE = 2;
    private static final Integer COLUMN_NUMBER_WITH_TX_AMOUNT_IN_EXCEL_FILE = 5;
    private static final Integer COLUMN_NUMBER_WITH_TX_DESCRIPTION_IN_EXCEL_FILE = 4;
    private static final Integer COLUMN_NUMBER_WITH_TX_DATE_IN_EXCEL_FILE = 0;
    private static final Integer COLUMN_NUMBER_WITH_TX_TIME_IN_EXCEL_FILE = 1;

    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final MoneyConvertor moneyConvertor;

    public PrivatBankConvertor(CategoryService categoryService, CurrencyService currencyService, MoneyConvertor moneyConvertor) {
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.moneyConvertor = moneyConvertor;
    }

    public Long getBalance(Map<Integer, List<String>> data) {
        return moneyConvertor.toCents(
                data.get(FIRST_ROW_NUMBER_WITH_DATA_IN_EXCEL_FILE).get(COLUMN_NUMBER_WITH_BALANCE_IN_EXCEL_FILE)
        );
    }

    public Currency getCurrency(Map<Integer, List<String>> data) {
        return currencyService.getCurrencyByNameUkr(
                data.get(FIRST_ROW_NUMBER_WITH_DATA_IN_EXCEL_FILE).get(COLUMN_NUMBER_WITH_CURRENCY_IN_EXCEL_FILE)
        );
    }

    public List<Transaction> convertTransactions(Map<Integer, List<String>> data, Wallet wallet) {
        return data.values().stream()
                // skip title
                .skip(FIRST_ROW_NUMBER_WITH_DATA_IN_EXCEL_FILE)
                // skip footer
                .limit(data.size() - FIRST_ROW_NUMBER_WITH_DATA_IN_EXCEL_FILE.longValue() - 1)
                .map(val -> new Transaction()
                        .setWallet(wallet)
                        .setAmountInCents(moneyConvertor.toCents(val.get(COLUMN_NUMBER_WITH_TX_AMOUNT_IN_EXCEL_FILE)))
                        .setCategory(convertCategory(val.get(COLUMN_NUMBER_WITH_CATEGORY_IN_EXCEL_FILE)))
                        .setCreatedAt(convertTime(val.get(COLUMN_NUMBER_WITH_TX_DATE_IN_EXCEL_FILE), val.get(COLUMN_NUMBER_WITH_TX_TIME_IN_EXCEL_FILE)))
                        .setDescription(val.get(COLUMN_NUMBER_WITH_TX_DESCRIPTION_IN_EXCEL_FILE))
                )
                .toList();
    }

    private Category convertCategory(String privatBankCategory) {
        return categoryService.getCategoryByPrivatBankCategoryName(privatBankCategory);
    }

    private Instant convertTime(String date, String time) {
        return LocalDateTime.parse(StringUtils.join(date, ":", time), FORMAT)
                .toInstant(ZoneOffset.UTC);
    }
}
