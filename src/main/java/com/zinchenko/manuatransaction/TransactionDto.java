package com.zinchenko.manuatransaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TransactionDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("amount_in_units")
    private Double amountInUnits;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("wallet_id")
    private Integer walletId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public TransactionDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmountInUnits() {
        return amountInUnits;
    }

    public TransactionDto setAmountInUnits(Double amountInUnits) {
        this.amountInUnits = amountInUnits;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public TransactionDto setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public TransactionDto setWalletId(Integer walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TransactionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public TransactionDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
