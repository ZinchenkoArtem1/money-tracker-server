package com.zinchenko.monobank.wallet;

import com.zinchenko.admin.currency.Currency;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.UserService;
import com.zinchenko.user.model.User;
import com.zinchenko.monobank.transaction.MonobankTransaction;
import com.zinchenko.monobank.transaction.MonobankTransactionRepository;
import com.zinchenko.monobank.wallet.dto.ClientAccountResponse;
import com.zinchenko.monobank.wallet.dto.CreateMonobankWalletRequest;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.MonobankConvertor;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MonobankWalletService {

    private static final Long MAX_TIME_FOR_FETCHING_STATEMENTS = 2682000L;
    private static final int MAX_COUNT_FOR_FETCH_STATEMENTS = 500;
    private final MonobankConvertor monobankConvertor;
    private final MonobankClient monobankClient;
    private final MonobankTransactionRepository monobankTransactionRepository;
    private final MonobankWalletRepository monobankWalletRepository;
    private final MonobankWalletConvertor monobankWalletConvertor;
    private final SecurityUserService securityUserService;
    private final UserService userService;
    private final CurrencyService currencyService;

    public MonobankWalletService(MonobankClient monobankClient, MonobankConvertor monobankConvertor,
                                 MonobankTransactionRepository monobankTransactionRepository,
                                 MonobankWalletRepository monobankWalletRepository,
                                 MonobankWalletConvertor monobankWalletConvertor,
                                 SecurityUserService securityUserService,
                                 UserService userService, CurrencyService currencyService) {
        this.monobankClient = monobankClient;
        this.monobankConvertor = monobankConvertor;
        this.monobankTransactionRepository = monobankTransactionRepository;
        this.monobankWalletRepository = monobankWalletRepository;
        this.monobankWalletConvertor = monobankWalletConvertor;
        this.securityUserService = securityUserService;
        this.userService = userService;
        this.currencyService = currencyService;
    }

    public List<ClientAccountResponse> getClientAccounts(String token) {
        GetClientInfoResponse getClientInfoResponse = monobankClient.getClientInfo(token);

        return getClientInfoResponse.getAccounts().stream()
                //filer fop and another account types that don't have card number
                .filter(a -> !a.getMaskedPan().isEmpty())
                .map(monobankConvertor::toClientAccountResponse)
                .toList();
    }

    //ToDo: change using .get(0)
    //ToDo: max 500 in one request
    //ToDo: max 1 month period for fetch transactions
    public void createMonobankWallet(CreateMonobankWalletRequest request) {
        Instant to = Instant.now();
        Instant from = to.minus(request.getSyncPeriod().getCount(), request.getSyncPeriod().getUnit());

        List<StatementResponse> statements = monobankClient.getStatements(
                request.getToken(),
                request.getAccountId(),
                from.getEpochSecond(),
                to.getEpochSecond()
        );
        User user = userService.getUserByEmail(securityUserService.getActiveUser().getUsername());
        Currency currency = currencyService.getCurrencyByCode(statements.get(0).getCurrencyCode());

        MonobankWallet monobankWallet = monobankWalletRepository.save(monobankWalletConvertor.toMonobankWallet(
                request, currency, statements.get(0).getBalance(), user, to)
        );

        addTransactions(statements, monobankWallet);
    }

    public void syncMonobankWalletTransactions(Integer walletId) {
        MonobankWallet monobankWallet = monobankWalletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalStateException(""));
        Instant to = Instant.now();

        List<StatementResponse> statements = monobankClient.getStatements(
                monobankWallet.getToken(),
                monobankWallet.getAccountId(),
                monobankWallet.getLastSyncDate().getEpochSecond(),
                to.getEpochSecond()
        );

        addTransactions(statements, monobankWallet);
        monobankWalletRepository.save(monobankWallet.setLastSyncDate(to));
    }

    private void addTransactions(List<StatementResponse> statements, MonobankWallet monobankWallet) {
        List<MonobankTransaction> transactions = statements.stream()
                .map(st -> monobankWalletConvertor.toMonobankTransaction(st, monobankWallet))
                .toList();

        monobankTransactionRepository.saveAll(transactions);
    }
}
