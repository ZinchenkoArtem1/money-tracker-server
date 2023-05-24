package com.zinchenko.monobank;

import com.zinchenko.RandomGenerator;
import com.zinchenko.monobank.domain.MonobankData;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.transaction.domain.Transaction;
import com.zinchenko.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MonobankConvertorTest extends RandomGenerator {

    private MonobankConvertor monobankConvertor;

    @BeforeEach
    void setUp() {
        monobankConvertor = new MonobankConvertor();
    }

    @Test
    void toClientAccountResponseTest() {
        AccountResponse accountResponse = random(AccountResponse.class);

        ClientAccountResponse clientAccountResponse = monobankConvertor.toClientAccountResponse(accountResponse);

        assertEquals(accountResponse.getId(), clientAccountResponse.getId());
        assertEquals(accountResponse.getMaskedPan().get(0), clientAccountResponse.getMaskedPan());
    }

    @Test
    void toMonobankDataTest() {
        Wallet wallet = random(Wallet.class);
        Instant syncDate = Instant.now();
        String token = random(String.class);
        String accountId = random(String.class);

        MonobankData monobankData = monobankConvertor.toMonobankData(wallet, syncDate, token, accountId);

        assertEquals(wallet, monobankData.getWallet());
        assertEquals(syncDate, monobankData.getLastSyncDate());
        assertEquals(token, monobankData.getToken());
        assertEquals(accountId, monobankData.getAccountId());
    }

    @Test
    void fromMonobankTransactionTest() {
        StatementResponse statementResponse = random(StatementResponse.class);
        Wallet wallet = random(Wallet.class);

        Transaction transaction = monobankConvertor.fromMonobankTransaction(statementResponse, wallet);

        assertEquals(statementResponse.getDescription(), transaction.getDescription());
        assertEquals(wallet, transaction.getWallet());
        assertEquals(statementResponse.getAmount(), transaction.getAmountInCents());
    }
}
