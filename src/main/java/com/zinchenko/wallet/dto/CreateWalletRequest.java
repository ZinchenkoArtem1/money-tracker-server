package com.zinchenko.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zinchenko.wallet.domain.WalletType;

public class CreateWalletRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("wallet_type")
    private WalletType walletType;

    @JsonProperty("token")
    private String token;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("sync_period")
    private SyncPeriod syncPeriod;

    @JsonProperty("actual_balance_in_units")
    private Double actualBalanceInUnits;

    @JsonProperty("currency_id")
    private Integer currencyId;

    public String getName() {
        return name;
    }

    public CreateWalletRequest setName(String name) {
        this.name = name;
        return this;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public CreateWalletRequest setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public String getToken() {
        return token;
    }

    public CreateWalletRequest setToken(String token) {
        this.token = token;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public CreateWalletRequest setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public SyncPeriod getSyncPeriod() {
        return syncPeriod;
    }

    public CreateWalletRequest setSyncPeriod(SyncPeriod syncPeriod) {
        this.syncPeriod = syncPeriod;
        return this;
    }

    public Double getActualBalanceInUnits() {
        return actualBalanceInUnits;
    }

    public CreateWalletRequest setActualBalanceInUnits(Double actualBalanceInUnits) {
        this.actualBalanceInUnits = actualBalanceInUnits;
        return this;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public CreateWalletRequest setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }
}
