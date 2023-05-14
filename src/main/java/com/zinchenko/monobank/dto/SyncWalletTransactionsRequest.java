package com.zinchenko.monobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyncWalletTransactionsRequest {

    @JsonProperty("wallet_id")
    private Integer walletId;

    public Integer getWalletId() {
        return walletId;
    }

    public SyncWalletTransactionsRequest setWalletId(Integer walletId) {
        this.walletId = walletId;
        return this;
    }
}
