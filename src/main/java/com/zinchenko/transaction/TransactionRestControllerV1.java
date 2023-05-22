package com.zinchenko.transaction;


import com.zinchenko.monobank.MonobankTransactionService;
import com.zinchenko.transaction.dto.TransactionDto;
import com.zinchenko.transaction.manual.ManualTransactionService;
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
    private final MonobankTransactionService monobankTransactionService;
    private final ManualTransactionService manualTransactionService;

    public TransactionRestControllerV1(TransactionService transactionService, MonobankTransactionService monobankTransactionService,
                                       ManualTransactionService manualTransactionService) {
        this.transactionService = transactionService;
        this.monobankTransactionService = monobankTransactionService;
        this.manualTransactionService = manualTransactionService;
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
            throw new IllegalStateException("Transaction can be created only in manual wallet");
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public void update(@RequestBody TransactionDto transactionDto) {
        if (transactionDto.getWalletType() == WalletType.MANUAL) {
            manualTransactionService.update(transactionDto);
        } else if (transactionDto.getWalletType() == WalletType.MONOBANK) {
            monobankTransactionService.updateTransactionCategory(transactionDto);
        } else {
            throw new IllegalStateException("Unsupported wallet type");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        manualTransactionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
