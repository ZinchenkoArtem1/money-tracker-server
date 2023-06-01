package com.zinchenko.admin.currency;


import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/currencies")
public class CurrencyRestControllerV1 {

    private final CurrencyService currencyService;

    public CurrencyRestControllerV1(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<CurrencyDto>> findAll() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<CurrencyDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(currencyService.getCurrencyDto(id));
    }
}
