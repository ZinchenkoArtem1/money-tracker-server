package com.zinchenko.wallet;


import com.zinchenko.wallet.dto.CreateWalletRequest;
import com.zinchenko.wallet.dto.UpdateWalletRequest;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletRestControllerV1 {

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
}
