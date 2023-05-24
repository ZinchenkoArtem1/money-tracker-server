package com.zinchenko.transaction;


import com.zinchenko.manual.ManualTransactionService;
import com.zinchenko.monobank.MonobankTransactionService;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.wallet.domain.WalletType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionRestControllerV1 {

    private final TransactionService transactionService;
    private final ManualTransactionService manualTransactionService;
    private final MonobankTransactionService monobankTransactionService;

    public TransactionRestControllerV1(TransactionService transactionService, ManualTransactionService manualTransactionService,
                                       MonobankTransactionService monobankTransactionService) {
        this.transactionService = transactionService;
        this.manualTransactionService = manualTransactionService;
        this.monobankTransactionService = monobankTransactionService;
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
        if (transactionDto.getWalletType() == WalletType.MANUAL) {
            manualTransactionService.create(transactionDto);
        } else {
            throw new IllegalStateException();
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> update(@RequestBody TransactionDto transactionDto) {
        if (transactionDto.getWalletType() == WalletType.MANUAL) {
            manualTransactionService.update(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.MONOBANK) {
            monobankTransactionService.update(transactionDto);
        } else {
            throw new IllegalStateException();
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
