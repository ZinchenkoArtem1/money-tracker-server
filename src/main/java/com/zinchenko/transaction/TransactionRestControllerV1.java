package com.zinchenko.transaction;


import com.zinchenko.manual.ManualTransactionService;
import com.zinchenko.monobank.MonobankTransactionService;
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
    private final ManualTransactionService manualTransactionService;
    private final MonobankTransactionService monobankTransactionService;
    private final PrivatBankService privatBankService;

    public TransactionRestControllerV1(TransactionService transactionService, ManualTransactionService manualTransactionService,
                                       MonobankTransactionService monobankTransactionService, PrivatBankService privatBankService) {
        this.transactionService = transactionService;
        this.manualTransactionService = manualTransactionService;
        this.monobankTransactionService = monobankTransactionService;
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
        manualTransactionService.create(transactionDto);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> update(@RequestBody TransactionDto transactionDto) {
        if (transactionDto.getWalletType() == WalletType.MANUAL) {
            manualTransactionService.update(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.MONOBANK) {
            monobankTransactionService.update(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.PRIVATBANK) {
            privatBankService.updateTransaction(transactionDto);
        } else {
            throw new IllegalStateException("Not supported wallet file");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        manualTransactionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
