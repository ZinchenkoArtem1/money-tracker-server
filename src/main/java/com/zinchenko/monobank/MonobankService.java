package com.zinchenko.monobank;

import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.domain.MonobankDataRepository;
import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.dto.SyncWalletTransactionsRequest;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MonobankService {

    private final MonobankConvertor monobankConvertor;
    private final MonobankClient monobankClient;
    private final MonobankDataRepository monobankDataRepository;
    private final TransactionService transactionService;

    public MonobankService(MonobankClient monobankClient, MonobankConvertor monobankConvertor,
                           MonobankDataRepository monobankDataRepository, TransactionService transactionService) {
        this.monobankClient = monobankClient;
        this.monobankConvertor = monobankConvertor;
        this.monobankDataRepository = monobankDataRepository;
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

    public void syncMonobankWalletTransactions(SyncWalletTransactionsRequest syncWalletTransactionsRequest) {
        Integer walletId = syncWalletTransactionsRequest.getWalletId();
        MonobankData monobankData = monobankDataRepository.findByWalletId(walletId)
                .orElseThrow(() -> new IllegalStateException("Monobank data with wallet id [%s] not exist".formatted(walletId)));
        Instant to = Instant.now();

        List<StatementResponse> statements = monobankClient.getStatements(
                monobankData.getToken(),
                monobankData.getAccountId(),
                monobankData.getLastSyncDate().getEpochSecond(),
                to.getEpochSecond()
        );

        transactionService.addMonobankTransactions(statements, walletId);
        monobankDataRepository.save(monobankData.setLastSyncDate(to));
    }

    public void create(Wallet wallet, Instant lastSyncDate, String token, String accountId, List<StatementResponse> statements) {
        MonobankData monobankData = new MonobankData()
                .setWallet(wallet)
                .setLastSyncDate(lastSyncDate)
                .setToken(token)
                .setAccountId(accountId);
        monobankDataRepository.save(monobankData);

        transactionService.addMonobankTransactions(statements, wallet);
    }
}
