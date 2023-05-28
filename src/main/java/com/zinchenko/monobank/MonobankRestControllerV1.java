package com.zinchenko.monobank;


import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monobank")
public class MonobankRestControllerV1 {

    private final MonobankService monobankService;

    public MonobankRestControllerV1(MonobankService monobankService) {
        this.monobankService = monobankService;
    }

    @PostMapping("/getAccounts")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<ClientAccountResponse>> getClientAccounts(@RequestBody ClientAccountRequest clientAccountRequest) {
        return ResponseEntity.ok(monobankService.getClientAccounts(clientAccountRequest));
    }

    @PostMapping("/sync/{walletId}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> syncWalletTransactions(@PathVariable("walletId") Integer walletId) {
        monobankService.syncMonobankWallet(walletId);
        return ResponseEntity.ok().build();
    }
}
