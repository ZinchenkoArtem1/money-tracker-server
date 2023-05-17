package com.zinchenko.statistic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetStatisticRequest {

    @JsonProperty("wallet_id")
    private Integer walletId;

    @JsonProperty("from")
    private Long from;

    @JsonProperty("to")
    private Long to;

    public Integer getWalletId() {
        return walletId;
    }

    public GetStatisticRequest setWalletId(Integer walletId) {
        this.walletId = walletId;
        return this;
    }

    public Long getFrom() {
        return from;
    }

    public GetStatisticRequest setFrom(Long from) {
        this.from = from;
        return this;
    }

    public Long getTo() {
        return to;
    }

    public GetStatisticRequest setTo(Long to) {
        this.to = to;
        return this;
    }
}
