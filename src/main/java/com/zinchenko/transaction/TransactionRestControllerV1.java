package com.zinchenko.transaction;


import com.zinchenko.manual.ManualService;
import com.zinchenko.monobank.MonobankService;
import com.zinchenko.privatbank.PrivatBankService;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.WalletType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionRestControllerV1 {

    private final TransactionService transactionService;
    private final ManualService manualService;
    private final MonobankService monobankService;
    private final PrivatBankService privatBankService;

    public TransactionRestControllerV1(TransactionService transactionService, ManualService manualService,
                                       MonobankService monobankService, PrivatBankService privatBankService) {
        this.transactionService = transactionService;
        this.manualService = manualService;
        this.monobankService = monobankService;
        this.privatBankService = privatBankService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<TransactionDto>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/wallet/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<TransactionDto>> findAllByWallet(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(transactionService.findAllByWallet(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<TransactionDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(transactionService.getTransactionDto(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:all')")
    public void create(@RequestBody TransactionDto transactionDto) {
        manualService.createTransaction(transactionDto);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> update(@RequestBody TransactionDto transactionDto) {
        if (transactionDto.getWalletType() == WalletType.MANUAL) {
            manualService.updateTransaction(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.MONOBANK) {
            monobankService.updateTransactionCategory(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.PRIVATBANK) {
            privatBankService.updateTransactionCategory(transactionDto);
        } else {
            throw new IllegalStateException("Not supported wallet file");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        manualService.deleteTransactionById(id);
        return ResponseEntity.ok().build();
    }
}
