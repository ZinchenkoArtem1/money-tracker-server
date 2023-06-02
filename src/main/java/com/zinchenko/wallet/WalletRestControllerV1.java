package com.zinchenko.wallet;


import com.zinchenko.manual.ManualService;
import com.zinchenko.monobank.MonobankService;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletRestControllerV1 {

    private final WalletService walletService;
    private final MonobankService monobankService;
    private final ManualService manualService;

    public WalletRestControllerV1(WalletService walletService, MonobankService monobankService,
                                  ManualService manualService) {
        this.walletService = walletService;
        this.monobankService = monobankService;
        this.manualService = manualService;
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
        if (createWalletRequest.getWalletType() == WalletType.MONOBANK) {
            monobankService.createWallet(createWalletRequest);
        } else if (createWalletRequest.getWalletType() == WalletType.MANUAL) {
            manualService.createWallet(createWalletRequest);
        } else {
            throw new IllegalStateException();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        walletService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
