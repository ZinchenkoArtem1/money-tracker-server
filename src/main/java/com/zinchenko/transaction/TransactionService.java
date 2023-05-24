package com.zinchenko.transaction;

import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.transaction.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConvertor transactionConvertor;

    public TransactionService(TransactionRepository transactionRepository, TransactionConvertor transactionConvertor) {
        this.transactionRepository = transactionRepository;
        this.transactionConvertor = transactionConvertor;
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
        transactionRepository.saveAll(transactions);
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void delete(Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
