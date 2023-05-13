package com.zinchenko.transaction;

import com.zinchenko.admin.category.Category;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConvertor transactionConvertor;
    private final CategoryService categoryService;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, TransactionConvertor transactionConvertor, CategoryService categoryService,
                              WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.transactionConvertor = transactionConvertor;
        this.categoryService = categoryService;
        this.walletService = walletService;
    }

    public List<TransactionDto> findAllByWallet(Integer id) {
        return transactionRepository.findAllByWalletId(id).stream()
                .map(transactionConvertor::toDto)
                .toList();
    }

    public TransactionDto getTransactionDto(Integer id) {
        return transactionConvertor.toDto(getTransaction(id));
    }

    public Transaction getTransaction(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Transaction with id [%s] not found".formatted(id)));
    }

    public void create(TransactionDto transactionDto) {
        if (transactionDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create transaction operation");
        } else {
            Category category = categoryService.getCategory(transactionDto.getCategoryId());
            Wallet wallet = walletService.getWallet(transactionDto.getWalletId());
            Transaction transaction = transactionConvertor.fromDto(transactionDto, category, wallet);
            //ToDo: check maybe need save wallet with new actual balance
            wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() + transaction.getAmountInCents());
            transactionRepository.save(transaction);
        }
    }

    public void update(TransactionDto transactionDto) {
        checkExist(transactionDto.getId());
        Transaction transaction = getTransaction(transactionDto.getId());
        Category category = categoryService.getCategory(transactionDto.getCategoryId());
        Wallet wallet = transaction.getWallet();
        long newTransactionAmountInCents = Double.valueOf(transactionDto.getAmountInUnits() * 100).longValue();
        wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() - transaction.getAmountInCents() + newTransactionAmountInCents);
        transaction.setDescription(transactionDto.getDescription())
                .setAmountInCents(newTransactionAmountInCents)
                .setCategory(category);
        transactionRepository.save(transaction);
    }

    public void deleteById(Integer id) {
        checkExist(id);
        Transaction transaction = getTransaction(id);
        Wallet wallet = transaction.getWallet();
        wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() - transaction.getAmountInCents());
        transactionRepository.deleteById(id);
    }

    private void checkExist(Integer id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalStateException("Transaction with id [%s] not found".formatted(id));
        }
    }
}
