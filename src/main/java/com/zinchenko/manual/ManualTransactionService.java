package com.zinchenko.manual;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.TransactionConvertor;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

@Service
public class ManualTransactionService {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final WalletService walletService;
    private final TransactionConvertor transactionConvertor;
    private final MoneyConvertor moneyConvertor;

    public ManualTransactionService(TransactionService transactionService, CategoryService categoryService,
                                    WalletService walletService, TransactionConvertor transactionConvertor,
                                    MoneyConvertor moneyConvertor) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.walletService = walletService;
        this.transactionConvertor = transactionConvertor;
        this.moneyConvertor = moneyConvertor;
    }

    public void create(TransactionDto transactionDto) {
        if (transactionDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create transaction operation");
        } else {
            Category category = categoryService.getCategory(transactionDto.getCategoryId());
            Wallet wallet = walletService.getWallet(transactionDto.getWalletId());
            Transaction transaction = transactionConvertor.fromDto(transactionDto, category, wallet);
            wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() + transaction.getAmountInCents());
            transactionService.save(transaction);
        }
    }

    public void deleteById(Integer id) {
        transactionService.checkExist(id);
        Transaction transaction = transactionService.getTransaction(id);

        walletService.updateBalance(
                transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents()
        );

        transactionService.delete(id);
    }

    public void update(TransactionDto transactionDto) {
        transactionService.checkExist(transactionDto.getId());

        Transaction transaction = transactionService.getTransaction(transactionDto.getId());
        Category category = categoryService.getCategory(transactionDto.getCategoryId());
        Long transactionAmountInCents = moneyConvertor.toCents(transactionDto.getAmountInUnits());

        walletService.updateBalance(
                transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents() + transactionAmountInCents
        );

        transaction.setDescription(transactionDto.getDescription())
                .setAmountInCents(transactionAmountInCents)
                .setCategory(category);

        transactionService.save(transaction);
    }
}
