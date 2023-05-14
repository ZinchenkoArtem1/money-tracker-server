package com.zinchenko.monobank;

import com.zinchenko.monobank.dto.ClientAccountResponse;
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
