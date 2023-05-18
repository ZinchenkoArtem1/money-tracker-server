package com.zinchenko.monobank;


import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.common.error.GenericException;
import com.zinchenko.monobank.dto.ClientAccountRequest;
import com.zinchenko.monobank.dto.ClientAccountResponse;
import com.zinchenko.monobank.dto.SyncWalletTransactionsRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/monobank")
public class MonobankRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(MonobankRestControllerV1.class);
    private final MonobankService monobankService;

    public MonobankRestControllerV1(MonobankService monobankService) {
        this.monobankService = monobankService;
    }

    @PostMapping("/getAccounts")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<ClientAccountResponse>> getClientAccounts(@RequestBody ClientAccountRequest clientAccountRequest) {
        return ResponseEntity.ok(monobankService.getClientAccounts(clientAccountRequest));
    }

    @PostMapping("/sync")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> syncWalletTransactions(@RequestBody SyncWalletTransactionsRequest syncWalletTransactionsRequest) {
        monobankService.syncMonobankWalletTransactions(syncWalletTransactionsRequest);
        return ResponseEntity.ok().build();
    }
}
