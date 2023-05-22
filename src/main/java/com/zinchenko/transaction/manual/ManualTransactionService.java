package com.zinchenko.transaction.manual;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.transaction.TransactionConvertor;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import org.springframework.stereotype.Service;

@Service
public class ManualTransactionService {

    private final TransactionService transactionService;
    private final WalletService walletService;
    private final CategoryService categoryService;
    private final TransactionConvertor transactionConvertor;
    private final MoneyConvertor moneyConvertor;

    public ManualTransactionService(TransactionService transactionService, WalletService walletService,
                                    CategoryService categoryService, TransactionConvertor transactionConvertor,
                                    MoneyConvertor moneyConvertor) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.categoryService = categoryService;
        this.transactionConvertor = transactionConvertor;
        this.moneyConvertor = moneyConvertor;
    }

    public void deleteById(Integer id) {
        transactionService.checkExist(id);
        Transaction transaction = transactionService.getTransaction(id);

        if (transaction.getWallet().getWalletType() != WalletType.MANUAL) {
            throw new IllegalStateException("Transaction can be deleted only in manual wallet");
        }

        walletService.updateBalance(
                transaction.getWallet().getWalletId(),
                transaction.getWallet().getActualBalanceInCents() - transaction.getAmountInCents()
        );

        transactionService.delete(id);
    }

    public void create(TransactionDto transactionDto) {
        if (transactionDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create transaction operation");
        } else {
            Category category = categoryService.getCategory(transactionDto.getCategoryId());
            Wallet wallet = walletService.getWallet(transactionDto.getWalletId());
            Transaction transaction = transactionConvertor.fromManualTransaction(transactionDto, category, wallet);
            wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() + transaction.getAmountInCents());
            transactionService.save(transaction);
        }
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
