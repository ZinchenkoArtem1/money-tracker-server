package com.zinchenko.admin.currency;


import com.zinchenko.admin.currency.dto.CurrencyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        currencyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> create(@RequestBody CurrencyDto currencyDto) {
        currencyService.create(currencyDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> update(@RequestBody CurrencyDto currencyDto) {
        currencyService.update(currencyDto);
        return ResponseEntity.ok().build();
    }
}
