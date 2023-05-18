package com.zinchenko.transaction;


import com.zinchenko.transaction.dto.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionRestControllerV1 {

    private final TransactionService transactionService;

    public TransactionRestControllerV1(TransactionService transactionService) {
        this.transactionService = transactionService;
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
        transactionService.create(transactionDto);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public void update(@RequestBody TransactionDto transactionDto) {
        transactionService.update(transactionDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        transactionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
