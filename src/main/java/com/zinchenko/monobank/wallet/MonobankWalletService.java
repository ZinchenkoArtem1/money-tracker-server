package com.zinchenko.monobank.wallet;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.MonobankConvertor;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.monobank.transaction.domain.MonobankTransaction;
import com.zinchenko.monobank.transaction.domain.MonobankTransactionRepository;
import com.zinchenko.monobank.wallet.dto.ClientAccountResponse;
import com.zinchenko.monobank.wallet.dto.CreateMonobankWalletRequest;
import com.zinchenko.monobank.wallet.dto.MonobankWalletDto;
import com.zinchenko.monobank.wallet.domain.MonobankWallet;
import com.zinchenko.monobank.wallet.domain.MonobankWalletRepository;
import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.UserService;
import com.zinchenko.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MonobankWalletService {

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

    public List<MonobankWalletDto> findAllByUser() {
        return monobankWalletRepository.findAllByUserEmail(securityUserService.getActiveUser().getUsername()).stream()
                .map(monobankWalletConvertor::toDto)
                .toList();
    }

    public MonobankWalletDto getMonobankWalletDto(Integer id) {
        MonobankWallet monobankWallet = getMonobankWallet(id);
        return monobankWalletConvertor.toDto(monobankWallet);
    }

    public MonobankWallet getMonobankWallet(Integer id) {
        return monobankWalletRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Monobank wallet with id [%s] not exist".formatted(id)));
    }

    public void update(MonobankWalletDto monobankWalletDto) {
        checkExist(monobankWalletDto.getId());
        MonobankWallet monobankWallet = getMonobankWallet(monobankWalletDto.getId());
        monobankWallet.setName(monobankWalletDto.getName());
        monobankWalletRepository.save(monobankWallet);
    }

    public void deleteById(Integer id) {
        checkExist(id);
        monobankWalletRepository.deleteById(id);
    }

    private void checkExist(Integer id) {
        if (!monobankWalletRepository.existsById(id)) {
            throw new IllegalStateException("Monobank wallet with id [%s] not found".formatted(id));
        }
    }

    private void addTransactions(List<StatementResponse> statements, MonobankWallet monobankWallet) {
        List<MonobankTransaction> transactions = statements.stream()
                .map(st -> monobankWalletConvertor.toMonobankTransaction(
                        st, monobankWallet)
                )
                .toList();

        monobankTransactionRepository.saveAll(transactions);
    }
}
