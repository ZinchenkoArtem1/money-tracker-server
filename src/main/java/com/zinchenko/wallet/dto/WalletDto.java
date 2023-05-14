package com.zinchenko.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zinchenko.wallet.domain.WalletType;

public class WalletDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("actual_balance_in_units")
    private Double actualBalanceInUnits;

    @JsonProperty("currency_id")
    private Integer currencyId;

    @JsonProperty("currency_name")
    private String currencyName;

    @JsonProperty("currency_code")
    private Integer currencyCode;

    @JsonProperty("wallet_type")
    private WalletType walletType;

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

    public Double getActualBalanceInUnits() {
        return actualBalanceInUnits;
    }

    public WalletDto setActualBalanceInUnits(Double actualBalanceInUnits) {
        this.actualBalanceInUnits = actualBalanceInUnits;
        return this;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public WalletDto setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public WalletDto setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public WalletDto setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public WalletDto setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }
}
