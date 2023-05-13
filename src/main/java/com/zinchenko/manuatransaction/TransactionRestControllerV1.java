package com.zinchenko.manuatransaction;


import com.zinchenko.common.error.BasicErrorResponse;
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
@RequestMapping("/api/v1/transactions")
public class TransactionRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(TransactionRestControllerV1.class);
    private final TransactionService transactionService;

    public TransactionRestControllerV1(TransactionService transactionService) {
        this.transactionService = transactionService;
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
