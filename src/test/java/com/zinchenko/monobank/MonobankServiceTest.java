package com.zinchenko.monobank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.CategoryService;
import com.zinchenko.admin.currency.CurrencyService;
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
import com.zinchenko.wallet.WalletService;
import com.zinchenko.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonobankServiceTest extends RandomGenerator {

    @Mock
    private MonobankConvertor monobankConvertor;
    @Mock
    private MonobankClient monobankClient;
    @Mock
    private MonobankDataRepository monobankDataRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private CategoryService categoryService;

    private MonobankService monobankService;

    @BeforeEach
    void setUp() {
        monobankService = new MonobankService(monobankClient, monobankConvertor, monobankDataRepository, walletService,
                transactionService, currencyService, categoryService);
    }

    @Test
    void getClientAccountsTest() {
        String token = random(String.class);
        AccountResponse accountResponse = random(AccountResponse.class);
        ClientAccountResponse clientAccountResponse = random(ClientAccountResponse.class);

        GetClientInfoResponse getClientInfoResponse = new GetClientInfoResponse()
                .setAccounts(List.of(accountResponse));

        when(monobankClient.getClientInfo(token)).thenReturn(getClientInfoResponse);
        when(monobankConvertor.toClientAccountResponse(accountResponse)).thenReturn(clientAccountResponse);

        List<ClientAccountResponse> accountResponses = monobankService.getClientAccounts(new ClientAccountRequest().setToken(token));
        assertEquals(1, accountResponses.size());
        assertEquals(clientAccountResponse, accountResponses.get(0));
    }

    @Test
    void syncMonobankWalletTest() {
        Integer walletId = random(Integer.class);
        MonobankData monobankData = random(MonobankData.class);
        AccountResponse accountResponse = random(AccountResponse.class)
                .setId(monobankData.getAccountId());
        StatementResponse statementResponse = random(StatementResponse.class);
        Wallet wallet = random(Wallet.class);
        Transaction transaction = random(Transaction.class);

        when(monobankClient.getClientInfo(monobankData.getToken())).thenReturn(new GetClientInfoResponse().setAccounts(List.of(accountResponse)));
        when(monobankDataRepository.findByWalletId(walletId)).thenReturn(Optional.of(monobankData));
        when(monobankClient.getStatements(eq(monobankData.getToken()), eq(monobankData.getAccountId()), any(), any()))
                .thenReturn(List.of(statementResponse));
        when(walletService.getWallet(walletId)).thenReturn(wallet);
        when(monobankConvertor.fromMonobankTransaction(statementResponse, wallet)).thenReturn(transaction);

        monobankService.syncWallet(walletId);

        verify(transactionService).saveAll(List.of(transaction));
        verify(walletService).updateBalance(walletId, accountResponse.getBalance());
        verify(monobankDataRepository).save(monobankData);
    }

//    @Test
//    void getAccountTest() {
//        CreateWalletRequest createWalletRequest = random(CreateWalletRequest.class);
//        AccountResponse accountResponse = random(AccountResponse.class)
//                .setId(createWalletRequest.getAccountId());
//
//        when(monobankClient.getClientInfo(createWalletRequest.getToken())).thenReturn(new GetClientInfoResponse()
//                .setAccounts(List.of(accountResponse)));
//
//        assertEquals(accountResponse, monobankService.getAccount(createWalletRequest));
//    }
//
//    @Test
//    void createMonobankDataTest() {
//        CreateWalletRequest createWalletRequest = random(CreateWalletRequest.class);
//        Wallet wallet = random(Wallet.class);
//        Instant to = Instant.now();
//        MonobankData monobankData = random(MonobankData.class);
//
//        when(monobankConvertor.toMonobankData(wallet, to,
//                createWalletRequest.getToken(), createWalletRequest.getAccountId())
//        ).thenReturn(monobankData);
//
//        monobankService.createMonobankData(createWalletRequest, wallet, to);
//
//        verify(monobankDataRepository).save(monobankData);
//    }
//
//    @Test
//    void updateTransactionsTest() {
//        CreateWalletRequest createWalletRequest = random(CreateWalletRequest.class);
//        Wallet wallet = random(Wallet.class);
//        Instant to = Instant.now();
//        StatementResponse statement = random(StatementResponse.class);
//        Transaction transaction = random(Transaction.class);
//
//        when(monobankConvertor.fromMonobankTransaction(statement, wallet)).thenReturn(transaction);
//        when(monobankClient.getStatements(
//                createWalletRequest.getToken(), createWalletRequest.getAccountId(),
//                to.minus(createWalletRequest.getSyncPeriod().getCount(), createWalletRequest.getSyncPeriod().getUnit()).getEpochSecond(),
//                to.getEpochSecond()
//        )).thenReturn(List.of(statement));
//
//        monobankService.updateTransactions(createWalletRequest, to, wallet);
//
//        verify(transactionService).saveAll(List.of(transaction));
//    }
}
