package com.zinchenko.privatbank;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/privatbank")
public class PrivatBankRestControllerV1 {

    private final PrivatBankService privatBankService;

    public PrivatBankRestControllerV1(PrivatBankService privatBankService) {
        this.privatBankService = privatBankService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> create(@RequestParam MultipartFile file, @RequestParam String name) {
        privatBankService.createWallet(file, name);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/update")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> update(@RequestParam MultipartFile file, @RequestParam Integer walletId) {
        privatBankService.updateWallet(file, walletId);
        return ResponseEntity.ok().build();
    }
}
