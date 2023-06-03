package com.zinchenko.manual;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private CategoryService categoryService;
    @Mock
    private CurrencyService currencyService;

    private ManualService manualService;

    @BeforeEach
    void setUp() {
        manualService = new ManualService(transactionService, categoryService,
                walletService, manualConvertor,
                new MoneyConvertor(), currencyService);
    }

    @Test
    void createWalletTest() {
        CreateWalletRequest request = random(CreateWalletRequest.class);
        Currency currency = random(Currency.class);

        when(currencyService.getCurrencyById(request.getCurrencyId())).thenReturn(currency);

        manualService.createWallet(request);

        verify(walletService).save(request.getName(), currency, request.getActualBalanceInUnits(), WalletType.MANUAL);
    }

    @Test
    void createTransactionTest() {
        TransactionDto transactionDto = random(TransactionDto.class).setId(null);
        Wallet wallet = random(Wallet.class);
        Category category = random(Category.class);
        Transaction transaction = random(Transaction.class);

        when(walletService.getWallet(transactionDto.getWalletId())).thenReturn(wallet);
        when(categoryService.getCategoryById(transactionDto.getCategoryId())).thenReturn(category);
        when(manualConvertor.toManualTransaction(transactionDto, category, wallet)).thenReturn(transaction);

        manualService.createTransaction(transactionDto);

        verify(walletService).updateBalance(transactionDto.getWalletId(), wallet.getActualBalanceInCents() + transaction.getAmountInCents());
        verify(transactionService).save(transaction);
    }

    @Test
    void deleteTransactionByIdTest() {
        Integer id = RandomUtils.nextInt();
        Transaction transaction = random(Transaction.class);

        when(transactionService.getTransaction(id)).thenReturn(transaction);

        manualService.deleteTransactionById(id);

        verify(walletService).updateBalance(transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents()
        );
        verify(transactionService).delete(id);
    }

    @Test
    void updateTransactionTest() {
        TransactionDto transactionDto = random(TransactionDto.class).setId(null);
        Category category = random(Category.class);
        Transaction transaction = random(Transaction.class);
        Long oldTxAmount = transaction.getAmountInCents();

        when(categoryService.getCategoryById(transactionDto.getCategoryId())).thenReturn(category);
        when(transactionService.getTransaction(transactionDto.getId())).thenReturn(transaction);

        manualService.updateTransaction(transactionDto);

        verify(walletService).updateBalance(transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - oldTxAmount +
                        BigDecimal.valueOf(transactionDto.getAmountInUnits()).movePointRight(2).longValue()
        );
        verify(transactionService).save(transaction);
        assertEquals(transactionDto.getDescription(), transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(BigDecimal.valueOf(transactionDto.getAmountInUnits()).movePointRight(2).longValue(), transaction.getAmountInCents());
    }
}
