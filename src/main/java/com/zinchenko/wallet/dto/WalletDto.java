package com.zinchenko.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("initial_balance_in_units")
    private Double initialBalanceInUnits;

    @JsonProperty("actual_balance_in_units")
    private Double actualBalanceInUnits;

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

    public Double getInitialBalanceInUnits() {
        return initialBalanceInUnits;
    }

    public WalletDto setInitialBalanceInUnits(Double initialBalanceInUnits) {
        this.initialBalanceInUnits = initialBalanceInUnits;
        return this;
    }

    public Double getActualBalanceInUnits() {
        return actualBalanceInUnits;
    }

    public WalletDto setActualBalanceInUnits(Double actualBalanceInUnits) {
        this.actualBalanceInUnits = actualBalanceInUnits;
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
