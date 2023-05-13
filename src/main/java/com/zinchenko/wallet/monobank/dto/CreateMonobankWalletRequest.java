package com.zinchenko.wallet.monobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class CreateMonobankWalletRequest {

    @JsonProperty("token")
    private String token;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("sync_period")
    private SyncPeriod syncPeriod;

    @Column(name = "name")
    private String name;

    public String getToken() {
        return token;
    }

    public CreateMonobankWalletRequest setToken(String token) {
        this.token = token;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public CreateMonobankWalletRequest setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public SyncPeriod getSyncPeriod() {
        return syncPeriod;
    }

    public CreateMonobankWalletRequest setSyncPeriod(SyncPeriod syncPeriod) {
        this.syncPeriod = syncPeriod;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateMonobankWalletRequest setName(String name) {
        this.name = name;
        return this;
    }
}
