package com.zinchenko.monobank;

import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.domain.MonobankDataRepository;
import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MonobankService {

    private final MonobankConvertor monobankConvertor;
    private final MonobankClient monobankClient;
    private final MonobankDataRepository monobankDataRepository;

    private final WalletService walletService;
    private final TransactionService transactionService;

    private final CurrencyService currencyService;
    private final CategoryService categoryService;

    public MonobankService(MonobankClient monobankClient, MonobankConvertor monobankConvertor,
                           MonobankDataRepository monobankDataRepository, WalletService walletService,
                           TransactionService transactionService, CurrencyService currencyService,
                           CategoryService categoryService) {
        this.monobankClient = monobankClient;
        this.monobankConvertor = monobankConvertor;
        this.monobankDataRepository = monobankDataRepository;
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.currencyService = currencyService;
        this.categoryService = categoryService;
    }

    public void createWallet(CreateWalletRequest request) {
        // fetch actual wallet currency and balance
        AccountResponse accountResponse = getAccount(request.getToken(), request.getAccountId());
        Currency currency = currencyService.getCurrencyByCode(accountResponse.getCurrencyCode());
        Long balance = accountResponse.getBalance();

        // create wallet
        Wallet wallet = walletService.save(request.getName(), currency, balance, request.getWalletType());

        Instant to = Instant.now();
        // create special data for monobank wallet
        MonobankData monobankData = monobankConvertor.toMonobankData(wallet, to, request.getToken(), request.getAccountId());
        monobankDataRepository.save(monobankData);

        // add transactions to wallet for sync period
        List<StatementResponse> statements = monobankClient.getStatements(
                request.getToken(),
                request.getAccountId(),
                to.minus(request.getSyncPeriod().getCount(), request.getSyncPeriod().getUnit()).getEpochSecond(),
                to.getEpochSecond()
        );
        addMonobankTransactions(statements, wallet);
    }

    public void updateTransactionCategory(TransactionDto transactionDto) {
        Transaction transaction = transactionService.getTransaction(transactionDto.getId());
        Category category = categoryService.getCategoryById(transactionDto.getCategoryId());
        transaction.setCategory(category);

        transactionService.save(transaction);
    }

    public List<ClientAccountResponse> getClientAccounts(ClientAccountRequest clientAccountRequest) {
        GetClientInfoResponse getClientInfoResponse = monobankClient.getClientInfo(clientAccountRequest.getToken());

        return getClientInfoResponse.getAccounts().stream()
                //filer fop and another account types that don't have card number
                .filter(a -> !a.getMaskedPan().isEmpty())
                .map(monobankConvertor::toClientAccountResponse)
                .toList();
    }

    public void syncWallet(Integer walletId) {
        MonobankData monobankData = monobankDataRepository.findByWalletId(walletId)
                .orElseThrow(() -> new IllegalStateException("Monobank data with wallet id [%s] not exist".formatted(walletId)));
        AccountResponse accountResponse = getAccount(monobankData.getToken(), monobankData.getAccountId());

        // get transactions for period from monobank
        Instant to = Instant.now();
        List<StatementResponse> statements = monobankClient.getStatements(
                monobankData.getToken(),
                monobankData.getAccountId(),
                monobankData.getLastSyncDate().getEpochSecond(),
                to.getEpochSecond()
        );
        // if period has transaction -> add to system and update balance to actual
        if (!statements.isEmpty()) {
            addMonobankTransactions(statements, walletService.getWallet(walletId));
            walletService.updateBalance(walletId, accountResponse.getBalance());
        }

        // update last sync date
        monobankDataRepository.save(monobankData.setLastSyncDate(to));
    }

    private AccountResponse getAccount(String token, String accountId) {
        return monobankClient.getClientInfo(token).getAccounts().stream()
                .filter(ac -> ac.getId().equals(accountId))
                .findAny()
                .orElseThrow();
    }

    private void addMonobankTransactions(List<StatementResponse> statements, Wallet wallet) {
        List<Transaction> transactions = statements.stream()
                .map(st -> monobankConvertor.fromMonobankTransaction(st, wallet))
                .toList();

        if (!statements.isEmpty()) {
            transactionService.saveAll(transactions);
        }
    }
}
