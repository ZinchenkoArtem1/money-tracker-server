package com.zinchenko.monobank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonobankTransactionServiceTest extends RandomGenerator {

    @Mock
    private TransactionService transactionService;
    @Mock
    private CategoryService categoryService;

    private MonobankTransactionService monobankTransactionService;

    @BeforeEach
    void setUp() {
        monobankTransactionService = new MonobankTransactionService(transactionService, categoryService);
    }

    @Test
    void updateTest() {
        TransactionDto transactionDto = random(TransactionDto.class);
        Transaction transaction = random(Transaction.class);
        Category category = random(Category.class);

        when(categoryService.getCategoryById(transactionDto.getCategoryId())).thenReturn(category);
        when(transactionService.getTransaction(transactionDto.getId())).thenReturn(transaction);

        monobankTransactionService.update(transactionDto);

        assertEquals(category, transaction.getCategory());
        verify(transactionService).save(transaction);
    }
}
