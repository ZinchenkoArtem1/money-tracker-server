package com.zinchenko.statistic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class GetStatisticRequest {

    @JsonProperty("wallet_id")
    private Integer walletId;

    @JsonProperty("from")
    private Long from = Instant.now().minus(30, ChronoUnit.DAYS).getEpochSecond();

    @JsonProperty("to")
    private Long to = Instant.now().plus(1, ChronoUnit.SECONDS).getEpochSecond();

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
        if (from != null) {
            this.from = from;
        }
        return this;
    }

    public Long getTo() {
        return to;
    }

    public GetStatisticRequest setTo(Long to) {
        if (to != null) {
            this.to = to;
        }
        return this;
    }
}
