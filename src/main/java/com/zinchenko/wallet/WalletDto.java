package com.zinchenko.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zinchenko.admin.currency.Currency;

public class WalletDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("initial_balance")
    private Double initialBalance;

    @JsonProperty("actual_balance")
    private Double actualBalance;

    @JsonProperty("currency_id")
    private Integer currencyId;

    public Integer getId() {
        return id;
    }

    public WalletDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WalletDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public WalletDto setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
        return this;
    }

    public Double getActualBalance() {
        return actualBalance;
    }

    public WalletDto setActualBalance(Double actualBalance) {
        this.actualBalance = actualBalance;
        return this;
    }

    public WalletDto setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }
}
