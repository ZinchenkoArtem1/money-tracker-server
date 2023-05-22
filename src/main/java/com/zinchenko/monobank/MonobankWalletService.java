package com.zinchenko.monobank;

import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.common.money.MoneyConvertor;
import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.domain.MonobankDataRepository;
import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.dto.SyncWalletTransactionsRequest;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MonobankWalletService {

    private final MonobankConvertor monobankConvertor;
    private final MonobankClient monobankClient;
    private final MonobankDataRepository monobankDataRepository;
    private final MonobankTransactionService monobankTransactionService;
    private final CurrencyService currencyService;
    private final WalletService walletService;
    private final MoneyConvertor moneyConvertor;

    public MonobankWalletService(MonobankClient monobankClient, MonobankConvertor monobankConvertor,
                                 MonobankDataRepository monobankDataRepository, MonobankTransactionService monobankTransactionService,
                                 CurrencyService currencyService, WalletService walletService, MoneyConvertor moneyConvertor) {
        this.monobankClient = monobankClient;
        this.monobankConvertor = monobankConvertor;
        this.monobankDataRepository = monobankDataRepository;
        this.monobankTransactionService = monobankTransactionService;
        this.currencyService = currencyService;
        this.walletService = walletService;
        this.moneyConvertor = moneyConvertor;
    }

    public List<ClientAccountResponse> getClientAccounts(ClientAccountRequest clientAccountRequest) {
        GetClientInfoResponse getClientInfoResponse = monobankClient.getClientInfo(clientAccountRequest.getToken());

        return getClientInfoResponse.getAccounts().stream()
                //filer fop and another account types that don't have card number
                .filter(a -> !a.getMaskedPan().isEmpty())
                .map(monobankConvertor::toClientAccountResponse)
                .toList();
    }

    public void syncMonobankWallet(SyncWalletTransactionsRequest request) {
        Integer walletId = request.getWalletId();
        MonobankData monobankData = getMonobankData(walletId);
        Instant to = Instant.now();
        AccountResponse accountResponse = getAccount(monobankData.getToken(), monobankData.getAccountId());

        List<StatementResponse> statements = monobankClient.getStatements(
                monobankData.getToken(),
                monobankData.getAccountId(),
                monobankData.getLastSyncDate().getEpochSecond(),
                to.getEpochSecond()
        );
        if (!statements.isEmpty()) {
            monobankTransactionService.addMonobankTransactions(statements, walletId);
            walletService.updateBalance(walletId, accountResponse.getBalance());
        }

        monobankDataRepository.save(monobankData.setLastSyncDate(to));
    }

    public void createMonobankWallet(CreateWalletRequest request) {
        Instant now = Instant.now();
        AccountResponse accountResponse = getAccount(request.getToken(), request.getAccountId());

        Currency currency = currencyService.getCurrencyByCode(accountResponse.getCurrencyCode());
        Long balance = accountResponse.getBalance();
        Wallet wallet = walletService.save(
                request.getName(), currency,
                moneyConvertor.toUnits(balance),
                WalletType.MONOBANK
        );

        MonobankData monobankData = monobankConvertor.toMonobankData(wallet, now, request.getToken(), request.getAccountId());
        monobankDataRepository.save(monobankData);

        List<StatementResponse> statements = monobankClient.getStatements(
                request.getToken(),
                request.getAccountId(),
                now.minus(request.getSyncPeriod().getCount(), request.getSyncPeriod().getUnit()).getEpochSecond(),
                now.getEpochSecond()
        );
        monobankTransactionService.addMonobankTransactions(statements, wallet);
    }

    private AccountResponse getAccount(String token, String accountId) {
        return monobankClient.getClientInfo(token).getAccounts().stream()
                .filter(ac -> ac.getId().equals(accountId))
                .findAny()
                .orElseThrow();
    }

    private MonobankData getMonobankData(Integer walletId) {
        return monobankDataRepository.findByWalletId(walletId)
                .orElseThrow(() -> new IllegalStateException("Monobank data with wallet id [%s] not exist".formatted(walletId)));
    }
}
