package com.zinchenko.monobank;

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
import com.zinchenko.transaction.domain.Transaction;
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

    public MonobankService(MonobankClient monobankClient, MonobankConvertor monobankConvertor,
                           MonobankDataRepository monobankDataRepository, WalletService walletService,
                           TransactionService transactionService) {
        this.monobankClient = monobankClient;
        this.monobankConvertor = monobankConvertor;
        this.monobankDataRepository = monobankDataRepository;
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    public List<ClientAccountResponse> getClientAccounts(ClientAccountRequest clientAccountRequest) {
        GetClientInfoResponse getClientInfoResponse = monobankClient.getClientInfo(clientAccountRequest.getToken());

        return getClientInfoResponse.getAccounts().stream()
                //filer fop and another account types that don't have card number
                .filter(a -> !a.getMaskedPan().isEmpty())
                .map(monobankConvertor::toClientAccountResponse)
                .toList();
    }

    public void syncMonobankWallet(Integer walletId) {
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
            addMonobankTransactions(statements, walletService.getWallet(walletId));
            walletService.updateBalance(walletId, accountResponse.getBalance());
        }

        monobankDataRepository.save(monobankData.setLastSyncDate(to));
    }

    public AccountResponse getAccount(CreateWalletRequest request) {
        return getAccount(request.getToken(), request.getAccountId());
    }

    public void createMonobankData(CreateWalletRequest request, Wallet wallet, Instant to) {
        MonobankData monobankData = monobankConvertor.toMonobankData(wallet, to, request.getToken(), request.getAccountId());
        monobankDataRepository.save(monobankData);
    }

    public void updateTransactions(CreateWalletRequest request, Instant to, Wallet wallet) {
        List<StatementResponse> statements = monobankClient.getStatements(
                request.getToken(),
                request.getAccountId(),
                to.minus(request.getSyncPeriod().getCount(), request.getSyncPeriod().getUnit()).getEpochSecond(),
                to.getEpochSecond()
        );
        addMonobankTransactions(statements, wallet);
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

    private void addMonobankTransactions(List<StatementResponse> statements, Wallet wallet) {
        List<Transaction> transactions = statements.stream()
                .map(st -> monobankConvertor.fromMonobankTransaction(st, wallet))
                .toList();

        if (!statements.isEmpty()) {
            transactionService.saveAll(transactions);
        }
    }
}
