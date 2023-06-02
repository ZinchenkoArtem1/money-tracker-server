package com.zinchenko.privatbank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class PrivatBankService {

    private final PrivatBankExelParser privatBankExelParser;
    private final PrivatBankConvertor privatBankConvertor;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public PrivatBankService(WalletService walletService, TransactionService transactionService,
                             PrivatBankExelParser privatBankExelParser, PrivatBankConvertor privatBankConvertor, CategoryService categoryService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.privatBankExelParser = privatBankExelParser;
        this.privatBankConvertor = privatBankConvertor;
        this.categoryService = categoryService;
    }

    public void createWallet(MultipartFile file, String name) {
        Map<Integer, List<String>> data = privatBankExelParser.parseExelFile(file);

        Wallet wallet = walletService.save(
                name, privatBankConvertor.getCurrency(data),
                privatBankConvertor.getBalance(data), WalletType.PRIVATBANK
        );

        transactionService.saveAll(privatBankConvertor.convertTransactions(data, wallet));
    }

    public void updateWallet(MultipartFile file, Integer walletId) {
        Map<Integer, List<String>> data = privatBankExelParser.parseExelFile(file);
        Wallet wallet = walletService.getWallet(walletId);

        List<Transaction> transactions = privatBankConvertor.convertTransactions(data, wallet);

        if (isNeedUpdateBalance(transactions, walletId)) {
            wallet.setActualBalanceInCents(privatBankConvertor.getBalance(data));
        }

        // we save only new transactions that was now saved before
        transactionService.saveAll(findNewTransactions(wallet.getTransactions(), transactions));
    }

    public void updateTransactionCategory(TransactionDto transactionDto) {
        Transaction transaction = transactionService.getTransaction(transactionDto.getId());
        Category category = categoryService.getCategoryById(transactionDto.getCategoryId());
        transaction.setCategory(category);

        transactionService.save(transaction);
    }

    private List<Transaction> findNewTransactions(List<Transaction> oldTransactions, List<Transaction> newTransactions) {
        return newTransactions.stream()
                .filter(t -> isNotContains(oldTransactions, t))
                .toList();
    }

    private boolean isNotContains(List<Transaction> oldTransactions, Transaction transaction) {
        return oldTransactions.stream()
                .filter(t -> t.getCreatedAt().equals(transaction.getCreatedAt()) &&
                        t.getAmountInCents().equals(transaction.getAmountInCents()))
                .toList().isEmpty();
    }

    private boolean isNeedUpdateBalance(List<Transaction> newTransactions, Integer walletId) {
        Transaction newestFromOldTransactions = transactionService.getNewestTransaction(walletId);

        Transaction newestFromNewTransactions = newTransactions.stream()
                .max(Comparator.comparing(Transaction::getCreatedAt))
                .orElseThrow();

        return newestFromNewTransactions.getCreatedAt().compareTo(newestFromOldTransactions.getCreatedAt()) > 0;
    }
}

