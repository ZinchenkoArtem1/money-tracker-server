package com.zinchenko.statistic;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.statistic.dto.GetStatisticRequest;
import com.zinchenko.statistic.dto.GetStatisticResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.user.UserService;
import com.zinchenko.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest extends RandomGenerator {

    private StatisticService statisticService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        statisticService = new StatisticService(transactionRepository, userService);
    }

    @Test
    void getStatisticForWalletTest() {
        Integer walletId = random(Integer.class);
        long to = Instant.now().minus(1, ChronoUnit.DAYS).getEpochSecond();
        long from = Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond();
        Transaction transaction1 = random(Transaction.class)
                .setCategory(new Category().setName("category1"))
                .setAmountInCents(-100L);
        Transaction transaction2 = random(Transaction.class)
                .setCategory(new Category().setName("category2"))
                .setAmountInCents(100L);

        when(transactionRepository.findAllByWalletIdInPeriod(walletId, Instant.ofEpochSecond(from), Instant.ofEpochSecond(to))).thenReturn(List.of(transaction1, transaction2));

        GetStatisticResponse getStatisticResponse = statisticService.getStatisticByWallet(new GetStatisticRequest()
                .setWalletId(walletId)
                .setFrom(from)
                .setTo(to)
        );

        assertEquals("category1", getStatisticResponse.getExpenseStatistic().get(0).getCategory());
        assertEquals(1D, getStatisticResponse.getExpenseStatistic().get(0).getData());
        assertEquals("category2", getStatisticResponse.getIncomeStatistic().get(0).getCategory());
        assertEquals(1D, getStatisticResponse.getIncomeStatistic().get(0).getData());
    }

    @Test
    void getStatisticForAllWalletsTest() {
        long to = Instant.now().minus(1, ChronoUnit.DAYS).getEpochSecond();
        long from = Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond();
        Transaction transaction1 = random(Transaction.class)
                .setCategory(new Category().setName("category1"))
                .setAmountInCents(-100L);
        Transaction transaction2 = random(Transaction.class)
                .setCategory(new Category().setName("category2"))
                .setAmountInCents(100L);
        User user = random(User.class);

        when(userService.getActiveUser()).thenReturn(user);
        when(transactionRepository.findAllByUserInPeriod(user.getEmail(), Instant.ofEpochSecond(from), Instant.ofEpochSecond(to))).thenReturn(List.of(transaction1, transaction2));

        GetStatisticResponse getStatisticResponse = statisticService.getStatisticForAllWallets(new GetStatisticRequest()
                .setFrom(from)
                .setTo(to)
        );

        assertEquals("category1", getStatisticResponse.getExpenseStatistic().get(0).getCategory());
        assertEquals(1D, getStatisticResponse.getExpenseStatistic().get(0).getData());
        assertEquals("category2", getStatisticResponse.getIncomeStatistic().get(0).getCategory());
        assertEquals(1D, getStatisticResponse.getIncomeStatistic().get(0).getData());
    }
}
