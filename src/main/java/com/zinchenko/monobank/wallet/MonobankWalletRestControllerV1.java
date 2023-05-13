package com.zinchenko.monobank.wallet;


import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.monobank.wallet.dto.ClientAccountResponse;
import com.zinchenko.monobank.wallet.dto.CreateMonobankWalletRequest;
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
@RequestMapping("/api/v1/wallets/monobank")
public class MonobankWalletRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(MonobankWalletRestControllerV1.class);
    private final MonobankWalletService monobankWalletService;

    public MonobankWalletRestControllerV1(MonobankWalletService monobankWalletService) {
        this.monobankWalletService = monobankWalletService;
    }

    //ToDo: maybe change to post for more security
    @GetMapping("/{token}")
    public ResponseEntity<List<ClientAccountResponse>> getClientAccounts(@PathVariable("token") String token) {
        return ResponseEntity.ok(monobankWalletService.getClientAccounts(token));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> createMonobankWallet(@RequestBody CreateMonobankWalletRequest createMonobankWalletRequest) {
        monobankWalletService.createMonobankWallet(createMonobankWalletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> syncMonobankWalletTransactions(@PathVariable("id") Integer walletId) {
        monobankWalletService.syncMonobankWalletTransactions(walletId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicErrorResponse> handleException(Exception ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.internalServerError().body(
                new BasicErrorResponse("Internal server error")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BasicErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new BasicErrorResponse("Access Denied")
        );
    }
}
