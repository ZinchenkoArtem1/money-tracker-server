package com.zinchenko.monobank.transaction;


import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.manuatransaction.dto.TransactionDto;
import com.zinchenko.monobank.transaction.dto.MonobankTransactionDto;
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
@RequestMapping("/api/v1/transactions/monobank")
public class MonobankTransactionRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(MonobankTransactionRestControllerV1.class);
    private final MonobankTransactionService monobankTransactionService;

    public MonobankTransactionRestControllerV1(MonobankTransactionService monobankTransactionService) {
        this.monobankTransactionService = monobankTransactionService;
    }

    @GetMapping("/wallet/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<MonobankTransactionDto>> findAllByWallet(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(monobankTransactionService.findAllByWallet(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<MonobankTransactionDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(monobankTransactionService.getTransactionDto(id));
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('user:all')")
    public void update(@RequestBody MonobankTransactionDto monobankTransactionDto) {
        monobankTransactionService.update(monobankTransactionDto);
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
