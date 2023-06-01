package com.zinchenko.monobank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public class MonobankTransactionService {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public MonobankTransactionService(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    public void update(TransactionDto transactionDto) {
        Transaction transaction = transactionService.getTransaction(transactionDto.getId());
        Category category = categoryService.getCategoryById(transactionDto.getCategoryId());
        transaction.setCategory(category);

        transactionService.save(transaction);
    }
}
