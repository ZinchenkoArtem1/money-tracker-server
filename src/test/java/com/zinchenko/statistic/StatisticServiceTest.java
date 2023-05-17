package com.zinchenko.statistic;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.statistic.dto.GetStatisticRequest;
import com.zinchenko.statistic.dto.GetStatisticResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    private final static String EMAIL = "user@email";
    private final static Instant FROM = Instant.ofEpochSecond(1000);
    private final static Instant TO = Instant.ofEpochSecond(5000);
    private final static String PRODUCTS_CATEGORY = "products";
    private final static String CLOTHES_CATEGORY = "clothes";
    private final static String TRANSPORT_CATEGORY = "transport";

    private StatisticService statisticService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        statisticService = new StatisticService(transactionRepository);
    }

    @Test
    @DisplayName("""
            Get statistic for all wallets
            """)
    void getStatisticForAllWallets() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new User(EMAIL, "", List.of()), "", List.of()));

        when(transactionRepository.findAllByUserInPeriod(EMAIL, FROM, TO))
                .thenReturn(List.of(
                        generateTransaction(PRODUCTS_CATEGORY, 10L),
                        generateTransaction(PRODUCTS_CATEGORY, 20L),
                        generateTransaction(TRANSPORT_CATEGORY, 10L)
                ));

        GetStatisticResponse getStatisticResponse = statisticService.getStatisticForAllWallets(new GetStatisticRequest()
                .setFrom(FROM)
                .setTo(TO)
        );
    }

    private Transaction generateTransaction(String category, Long amount) {
        return new Transaction()
                .setCategory(new Category()
                        .setName(category))
                .setAmountInCents(amount);
    }
}
