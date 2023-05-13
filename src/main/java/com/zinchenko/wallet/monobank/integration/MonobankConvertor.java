package com.zinchenko.wallet.monobank.integration;

import com.zinchenko.wallet.monobank.dto.ClientAccountResponse;
import com.zinchenko.wallet.monobank.integration.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class MonobankConvertor {

    public ClientAccountResponse toClientAccountResponse(AccountResponse accountResponse) {
        return new ClientAccountResponse()
                .setId(accountResponse.getId())
                .setMaskedPan(accountResponse.getMaskedPan().get(0));
    }

}
