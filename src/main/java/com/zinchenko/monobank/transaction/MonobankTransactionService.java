package com.zinchenko.monobank.transaction;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.monobank.transaction.domain.MonobankTransaction;
import com.zinchenko.monobank.transaction.domain.MonobankTransactionRepository;
import com.zinchenko.monobank.transaction.dto.MonobankTransactionDto;
import com.zinchenko.monobank.wallet.MonobankWalletService;
import com.zinchenko.monobank.wallet.domain.MonobankWallet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonobankTransactionService {

    private final MonobankTransactionRepository monobankTransactionRepository;
    private final MonobankTransactionConvertor monobankTransactionConvertor;
    private final CategoryService categoryService;
    private final MonobankWalletService monobankWalletService;

    public MonobankTransactionService(MonobankTransactionRepository monobankTransactionRepository,
                                      MonobankTransactionConvertor monobankTransactionConvertor,
                                      CategoryService categoryService,
                                      MonobankWalletService monobankWalletService) {
        this.monobankTransactionRepository = monobankTransactionRepository;
        this.monobankTransactionConvertor = monobankTransactionConvertor;
        this.categoryService = categoryService;
        this.monobankWalletService = monobankWalletService;
    }

    public List<MonobankTransactionDto> findAllByWallet(Integer id) {
        return monobankTransactionRepository.findAllByWalletId(id).stream()
                .map(monobankTransactionConvertor::toDto)
                .toList();
    }

    public MonobankTransactionDto getTransactionDto(Integer id) {
        return monobankTransactionConvertor.toDto(getTransaction(id));
    }

    public MonobankTransaction getTransaction(Integer id) {
        return monobankTransactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Monobank transaction with id [%s] not found".formatted(id)));
    }

    public void update(MonobankTransactionDto monobankTransactionDto) {
        checkExist(monobankTransactionDto.getId());
        MonobankTransaction monobankTransaction = getTransaction(monobankTransactionDto.getId());
        Category category = categoryService.getCategory(monobankTransactionDto.getCategoryId());
        monobankTransaction
                .setDescription(monobankTransactionDto.getDescription())
                .setCategory(category);
        monobankTransactionRepository.save(monobankTransaction);
    }

    private void checkExist(Integer id) {
        if (!monobankTransactionRepository.existsById(id)) {
            throw new IllegalStateException("Monobank transaction with id [%s] not found".formatted(id));
        }
    }
}
