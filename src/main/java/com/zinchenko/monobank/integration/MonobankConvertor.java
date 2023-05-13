package com.zinchenko.monobank.integration;

import com.zinchenko.monobank.wallet.dto.ClientAccountResponse;
import com.zinchenko.monobank.integration.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class MonobankConvertor {

    public ClientAccountResponse toClientAccountResponse(AccountResponse accountResponse) {
        return new ClientAccountResponse()
                .setId(accountResponse.getId())
                .setMaskedPan(accountResponse.getMaskedPan().get(0));
    }

}
