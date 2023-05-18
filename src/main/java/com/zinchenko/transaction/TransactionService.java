package com.zinchenko.transaction;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConvertor transactionConvertor;
    private final CategoryService categoryService;
    private final WalletRepository walletRepository;
    private final MoneyConvertor moneyConvertor;

    public TransactionService(TransactionRepository transactionRepository, TransactionConvertor transactionConvertor,
                              CategoryService categoryService, WalletRepository walletRepository,
                              MoneyConvertor moneyConvertor) {
        this.transactionRepository = transactionRepository;
        this.transactionConvertor = transactionConvertor;
        this.categoryService = categoryService;
        this.walletRepository = walletRepository;
        this.moneyConvertor = moneyConvertor;
    }

    public List<TransactionDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionConvertor::toDto)
                .toList();
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
            Wallet wallet = getWallet(transactionDto.getWalletId());
            Transaction transaction = transactionConvertor.fromDto(transactionDto, category, wallet);
            wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() + transaction.getAmountInCents());
            transactionRepository.save(transaction);
        }
    }

    public void update(TransactionDto transactionDto) {
        checkExist(transactionDto.getId());
        Transaction transaction = getTransaction(transactionDto.getId());
        Category category = categoryService.getCategory(transactionDto.getCategoryId());
        Wallet wallet = transaction.getWallet();
        wallet.setActualBalanceInCents(wallet.getActualBalanceInCents() - transaction.getAmountInCents() + moneyConvertor.toCents(transactionDto.getAmountInUnits()));
        transaction.setDescription(transactionDto.getDescription())
                .setAmountInCents(moneyConvertor.toCents(transactionDto.getAmountInUnits()))
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

    public void addMonobankTransactions(List<StatementResponse> statements, Integer walletId) {
        addMonobankTransactions(statements, getWallet(walletId));
    }

    public void addMonobankTransactions(List<StatementResponse> statements, Wallet wallet) {
        List<Transaction> transactions = statements.stream()
                .map(st -> transactionConvertor.fromMonobankTransaction(st, wallet))
                .toList();

        transactionRepository.saveAll(transactions);
    }

    private void checkExist(Integer id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalStateException("Transaction with id [%s] not found".formatted(id));
        }
    }

    private Wallet getWallet(Integer id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wallet with id [%s] not exist".formatted(id)));
    }
}
