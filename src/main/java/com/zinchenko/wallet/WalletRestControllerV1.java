package com.zinchenko.wallet;


import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import com.zinchenko.wallet.dto.UpdateWalletRequest;
import com.zinchenko.wallet.dto.WalletDto;
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
@RequestMapping("/api/v1/wallets")
public class WalletRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(WalletRestControllerV1.class);
    private final WalletService walletService;

    public WalletRestControllerV1(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<WalletDto>> getAllUserWallets() {
        return ResponseEntity.ok(walletService.getAllUserWalletsDto());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<WalletDto> getWallet(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(walletService.getWalletDto(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:all')")
    public void create(@RequestBody CreateWalletRequest createWalletRequest) {
        walletService.create(createWalletRequest);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public void update(@RequestBody UpdateWalletRequest updateWalletRequest) {
        walletService.update(updateWalletRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        walletService.deleteById(id);
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
