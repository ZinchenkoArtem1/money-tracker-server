package com.zinchenko.transaction;

import com.zinchenko.monobank.CurrencyRateHolder;
import com.zinchenko.monobank.integration.dto.CurrencyRate;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConvertor transactionConvertor;
    private final UserService userService;
    private final CurrencyRateHolder currencyRateHolder;

    public TransactionService(TransactionRepository transactionRepository, TransactionConvertor transactionConvertor, UserService userService,
                              CurrencyRateHolder currencyRateHolder) {
        this.transactionRepository = transactionRepository;
        this.transactionConvertor = transactionConvertor;
        this.userService = userService;
        this.currencyRateHolder = currencyRateHolder;
    }

    public List<TransactionDto> findAll() {
        return transactionRepository.findAllByUserId(userService.getActiveUser().getUserId()).stream()
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

    public void checkExist(Integer id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalStateException("Transaction with id [%s] not found".formatted(id));
        }
    }

    public Transaction getTransaction(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Transaction with id [%s] not found".formatted(id)));
    }

    public void saveAll(List<Transaction> transactions) {
        CurrencyRate currencyRate = currencyRateHolder.getCurrencyRate(
                transactions.stream().findAny().orElseThrow().getWallet().getCurrency()
        );
        transactions.forEach(t -> t.setAmountInCentsUah(calcAmountInUah(t.getAmountInCents(), currencyRate)));
        transactionRepository.saveAll(transactions);
    }

    public void save(Transaction transaction) {
        CurrencyRate currencyRate = currencyRateHolder.getCurrencyRate(transaction.getWallet().getCurrency());
        transaction.setAmountInCentsUah(calcAmountInUah(transaction.getAmountInCents(), currencyRate));
        transactionRepository.save(transaction);
    }

    public void delete(Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    private Long calcAmountInUah(Long amountInCents, CurrencyRate currencyRate) {
        return BigDecimal.valueOf(currencyRate.getRateBuy() * amountInCents.doubleValue()).longValue();
    }
}
