package com.zinchenko.privatbank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrivatBankServiceTest extends RandomGenerator {

    private PrivatBankService privatBankService;

    @Mock
    private PrivatBankExelParser privatBankExelParser;
    @Mock
    private PrivatBankConvertor privatBankConvertor;
    @Mock
    private WalletService walletService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        privatBankService = new PrivatBankService(walletService, transactionService,
                privatBankExelParser, privatBankConvertor, categoryService);
    }

    @Test
    void createWalletTest() {
        String name = random(String.class);
        MultipartFile file = mock(MultipartFile.class);
        Map<Integer, List<String>> data = new HashMap<>();
        Currency currency = mock(Currency.class);
        Long balance = RandomUtils.nextLong();
        Wallet wallet = mock(Wallet.class);
        List<Transaction> transactions = List.of();

        when(privatBankExelParser.parseExelFile(file)).thenReturn(data);
        when(privatBankConvertor.getCurrency(data)).thenReturn(currency);
        when(privatBankConvertor.getBalance(data)).thenReturn(balance);
        when(walletService.save(name, currency, balance, WalletType.PRIVATBANK)).thenReturn(wallet);
        when(privatBankConvertor.convertTransactions(data, wallet)).thenReturn(transactions);

        privatBankService.createWallet(file, name);

        verify(transactionService).saveAll(transactions);
    }

    @Test
    @SuppressWarnings("unchecked")
    void updateWalletTest() {
        Integer walletId = RandomUtils.nextInt();
        MultipartFile file = mock(MultipartFile.class);
        Map<Integer, List<String>> data = new HashMap<>();
        Wallet wallet = mock(Wallet.class);
        Instant instant1 = Instant.now();
        Transaction transaction1 = new Transaction().setCreatedAt(instant1).setAmountInCents(100L);
        Transaction transaction2 = new Transaction().setCreatedAt(instant1.plus(1, ChronoUnit.DAYS)).setAmountInCents(150L);
        List<Transaction> transactions = List.of(transaction1, transaction2);
        ArgumentCaptor<List<Transaction>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        when(privatBankExelParser.parseExelFile(file)).thenReturn(data);
        when(walletService.getWallet(walletId)).thenReturn(wallet);
        when(privatBankConvertor.convertTransactions(data, wallet)).thenReturn(transactions);
        when(wallet.getTransactions()).thenReturn(List.of(transaction1));

        privatBankService.updateWallet(file, walletId);

        verify(transactionService).saveAll(listArgumentCaptor.capture());
        assertIterableEquals(List.of(transaction2), listArgumentCaptor.getValue());
    }

    @Test
    void updateTransactionTest() {
        TransactionDto transactionDto = random(TransactionDto.class);
        Transaction transaction = mock(Transaction.class);
        Category category = random(Category.class);

        when(transactionService.getTransaction(transactionDto.getId())).thenReturn(transaction);
        when(categoryService.getCategoryById(transactionDto.getCategoryId())).thenReturn(category);

        privatBankService.updateTransaction(transactionDto);

        verify(transaction).setCategory(category);
        verify(transactionService).save(transaction);
    }
}
