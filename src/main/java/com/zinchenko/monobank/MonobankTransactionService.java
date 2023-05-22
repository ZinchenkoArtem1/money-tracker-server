package com.zinchenko.monobank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonobankTransactionService {

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final MonobankConvertor monobankConvertor;
    private final MoneyConvertor moneyConvertor;
    private final CategoryService categoryService;

    public MonobankTransactionService(WalletService walletService, TransactionService transactionService,
                                      MonobankConvertor monobankConvertor, MoneyConvertor moneyConvertor,
                                      CategoryService categoryService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.monobankConvertor = monobankConvertor;
        this.moneyConvertor = moneyConvertor;
        this.categoryService = categoryService;
    }

    public void addMonobankTransactions(List<StatementResponse> statements, Integer walletId) {
        addMonobankTransactions(statements, walletService.getWallet(walletId));
    }

    public void addMonobankTransactions(List<StatementResponse> statements, Wallet wallet) {
        List<Transaction> transactions = statements.stream()
                .map(st -> monobankConvertor.fromMonobankTransaction(st, wallet))
                .toList();

        transactionService.saveAll(transactions);
    }

    public void updateTransactionCategory(TransactionDto transactionDto) {
        transactionService.checkExist(transactionDto.getId());
        Transaction transaction = transactionService.getTransaction(transactionDto.getId());
        Category category = categoryService.getCategory(transactionDto.getCategoryId());

        walletService.updateBalance(
                transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents() + moneyConvertor.toCents(transactionDto.getAmountInUnits())
        );

        transaction.setCategory(category);
        transactionService.update(transaction);
    }
}
