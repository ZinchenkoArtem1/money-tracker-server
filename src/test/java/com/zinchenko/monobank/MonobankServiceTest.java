package com.zinchenko.monobank;

import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.domain.MonobankDataRepository;
import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.dto.SyncWalletTransactionsRequest;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.monobank.integration.dto.GetClientInfoResponse;
import com.zinchenko.transaction.TransactionService;
import com.zinchenko.wallet.WalletService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonobankServiceTest {

    private final static String TOKEN = UUID.randomUUID().toString();
    @Mock
    private MonobankClient monobankClient;
    @Mock
    private MonobankDataRepository monobankDataRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private TransactionService transactionService;
    private MonobankService monobankService;

    @BeforeEach
    void setUp() {
        monobankService = new MonobankService(monobankClient, new MonobankConvertor(),
                monobankDataRepository, walletService, transactionService);
    }

    @Test
    void getClientAccountsTest() {
        String accountId1 = UUID.randomUUID().toString();
        String accountId2 = UUID.randomUUID().toString();
        String maskedPan = UUID.randomUUID().toString();

        GetClientInfoResponse getClientInfoResponse = new GetClientInfoResponse()
                .setAccounts(List.of(
                        new AccountResponse()
                                .setId(accountId1)
                                .setMaskedPan(List.of()),
                        new AccountResponse()
                                .setId(accountId2)
                                .setMaskedPan(List.of(maskedPan))
                ));

        when(monobankClient.getClientInfo(TOKEN)).thenReturn(getClientInfoResponse);

        List<ClientAccountResponse> accountResponses = monobankService.getClientAccounts(new ClientAccountRequest().setToken(TOKEN));
        assertEquals(1, accountResponses.size());
        assertEquals(maskedPan, accountResponses.get(0).getMaskedPan());
        assertEquals(accountId2, accountResponses.get(0).getId());
    }

//    @Test
//    void syncMonobankWallet() {
//        Integer walletId = RandomUtils.nextInt();
//        String accountId = UUID.randomUUID().toString();
//        MonobankData monobankData = mock(MonobankData.class);
//        GetClientInfoResponse getClientInfoResponse = mock(GetClientInfoResponse.class);
//        AccountResponse accountResponse = mock(AccountResponse.class);
//        Long newBalance = RandomUtils.nextLong();
//
//        when(accountResponse.getId()).thenReturn(accountId);
//        when(accountResponse.getBalance()).thenReturn(newBalance);
//        when(getClientInfoResponse.getAccounts()).thenReturn(List.of(accountResponse));
//        when(monobankData.getToken()).thenReturn(TOKEN);
//        when((monobankData.getAccountId())).thenReturn(accountId);
//        when(monobankDataRepository.findByWalletId(walletId)).thenReturn(Optional.of(monobankData));
//        when(monobankClient.getClientInfo(TOKEN)).thenReturn(getClientInfoResponse);
//        when(monobankClient.getStatements(eq(TOKEN), eq(accountId), any(), any())).thenReturn(List.of());
//        when(monobankData.getLastSyncDate()).thenReturn(Instant.now());
//
//        monobankService.syncMonobankWallet(new SyncWalletTransactionsRequest().setWalletId(walletId));
//
//        verify(walletService).updateBalance(walletId, newBalance);
//        verify(monobankData).setLastSyncDate(any());
//        verify(monobankDataRepository).save(monobankData);
//    }
}
