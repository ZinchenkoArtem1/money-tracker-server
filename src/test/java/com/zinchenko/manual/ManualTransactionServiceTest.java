package com.zinchenko.manual;

import com.zinchenko.AbstractTest;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManualTransactionServiceTest extends AbstractTest {

    private ManualTransactionService manualTransactionService;

    @Mock
    private TransactionService transactionService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private WalletService walletService;
    @Mock
    private ManualConvertor manualConvertor;

    @BeforeEach
    void setUp() {
        manualTransactionService = new ManualTransactionService(transactionService, categoryService, walletService,
                manualConvertor, new MoneyConvertor());
    }

    @Test
    void createFailedTest() {
        TransactionDto transactionDto = random(TransactionDto.class);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> manualTransactionService.create(transactionDto));
        assertEquals("Request body must not contain id for the create transaction operation", exc.getMessage());
    }

    @Test
    void createSuccessTest() {
        TransactionDto transactionDto = random(TransactionDto.class)
                .setId(null);
        Category category = random(Category.class);
        Wallet wallet = random(Wallet.class);
        Transaction transaction = random(Transaction.class);

        when(categoryService.getCategory(transactionDto.getCategoryId())).thenReturn(category);
        when(walletService.getWallet(transactionDto.getWalletId())).thenReturn(wallet);
        when(manualConvertor.toManualTransaction(transactionDto, category, wallet)).thenReturn(transaction);

        manualTransactionService.create(transactionDto);

        verify(transactionService).save(transaction);
        verify(walletService).updateBalance(transactionDto.getWalletId(),
                transaction.getAmountInCents() + wallet.getActualBalanceInCents()
        );
    }

    @Test
    void deleteById() {
        Integer id = random(Integer.class);
        Wallet wallet = random(Wallet.class);
        Transaction transaction = random(Transaction.class)
                .setWallet(wallet);

        when(transactionService.getTransaction(id)).thenReturn(transaction);

        manualTransactionService.deleteById(id);

        verify(walletService).updateBalance(transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents());
        verify(transactionService).delete(id);
    }

    @Test
    void update() {
        TransactionDto transactionDto = random(TransactionDto.class)
                .setAmountInUnits(100D);
        Wallet wallet = random(Wallet.class);
        Transaction transaction = random(Transaction.class)
                .setAmountInCents(10000L)
                .setWallet(wallet);
        Category category = random(Category.class);

        when(transactionService.getTransaction(transactionDto.getId())).thenReturn(transaction);
        when(categoryService.getCategory(transactionDto.getCategoryId())).thenReturn(category);

        manualTransactionService.update(transactionDto);

        verify(walletService).updateBalance(wallet.getWalletId(),
                wallet.getActualBalanceInCents() - transaction.getAmountInCents() + (transactionDto.getAmountInUnits().longValue() * 100));

        assertEquals(transactionDto.getDescription(), transaction.getDescription());
        assertEquals(transactionDto.getAmountInUnits().longValue() * 100, transaction.getAmountInCents());
        assertEquals(category, transaction.getCategory());
        verify(transactionService).save(transaction);
    }
}
