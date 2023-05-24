package com.zinchenko.statistic;

import com.zinchenko.statistic.dto.GetStatisticRequest;
import com.zinchenko.statistic.dto.GetStatisticResponse;
import com.zinchenko.statistic.dto.StatisticDto;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.domain.TransactionRepository;
import com.zinchenko.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public StatisticService(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public GetStatisticResponse getStatisticForAllWallets(GetStatisticRequest request) {
        String email = userService.getActiveUser().getEmail();
        List<Transaction> walletTransaction = transactionRepository.findAllByUserInPeriod(
                email, Instant.ofEpochSecond(request.getFrom()), Instant.ofEpochSecond(request.getTo())
        );

        return new GetStatisticResponse()
                .setIncomeStatistic(generateStatistic(
                        getIncomeTransactions(walletTransaction)
                ))
                .setExpenseStatistic(generateStatistic(
                        getExpenseTransactions(walletTransaction)
                ));
    }

    public GetStatisticResponse getStatisticByWallet(GetStatisticRequest request) {
        List<Transaction> walletTransaction = transactionRepository.findAllByWalletIdInPeriod(
                request.getWalletId(), Instant.ofEpochSecond(request.getFrom()), Instant.ofEpochSecond(request.getTo())
        );

        return new GetStatisticResponse()
                .setIncomeStatistic(generateStatistic(
                        getIncomeTransactions(walletTransaction)
                ))
                .setExpenseStatistic(generateStatistic(
                        getExpenseTransactions(walletTransaction)
                ));
    }

    private List<Transaction> getIncomeTransactions(List<Transaction> transaction) {
        return transaction.stream()
                .filter(t -> t.getAmountInCents() >= 0)
                .toList();
    }

    private List<Transaction> getExpenseTransactions(List<Transaction> transaction) {
        return transaction.stream()
                .filter(t -> t.getAmountInCents() < 0)
                .toList();
    }

    private List<StatisticDto> generateStatistic(List<Transaction> transactions) {
        long sum = transactions.stream()
                .mapToLong(Transaction::getAmountInCents)
                .sum();

        return transactions.stream()
                .collect(Collectors.groupingBy(
                                t -> t.getCategory().getName(),
                                Collectors.collectingAndThen(
                                        Collectors.summingLong(Transaction::getAmountInCents),
                                        categorySum -> BigDecimal.valueOf(
                                                categorySum.doubleValue() / sum
                                        ).setScale(2, RoundingMode.DOWN)
                                )
                        )
                ).entrySet().stream()
                .map(e -> new StatisticDto(e.getKey(), e.getValue().doubleValue()))
                .toList();
    }
}
