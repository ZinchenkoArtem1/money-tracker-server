package com.zinchenko.monobank.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.time.Instant;

public class MonobankWalletDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("actual_balance_in_units")
    private Double actualBalanceInUnits;

    @JsonProperty("currency_id")
    private Integer currencyId;

    @Column(name = "last_sync_date")
    private Instant lastSyncDate;

    public Integer getId() {
        return id;
    }

    public MonobankWalletDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MonobankWalletDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getActualBalanceInUnits() {
        return actualBalanceInUnits;
    }

    public MonobankWalletDto setActualBalanceInUnits(Double actualBalanceInUnits) {
        this.actualBalanceInUnits = actualBalanceInUnits;
        return this;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public MonobankWalletDto setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public Instant getLastSyncDate() {
        return lastSyncDate;
    }

    public MonobankWalletDto setLastSyncDate(Instant lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
        return this;
    }
}
