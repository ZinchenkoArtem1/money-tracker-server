package com.zinchenko.monobank.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MonobankTransactionDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("amount_in_units")
    private Double amountInUnits;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("monobank_wallet_id")
    private Integer monobankWalletId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public MonobankTransactionDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmountInUnits() {
        return amountInUnits;
    }

    public MonobankTransactionDto setAmountInUnits(Double amountInUnits) {
        this.amountInUnits = amountInUnits;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public MonobankTransactionDto setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Integer getMonobankWalletId() {
        return monobankWalletId;
    }

    public MonobankTransactionDto setMonobankWalletId(Integer monobankWalletId) {
        this.monobankWalletId = monobankWalletId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MonobankTransactionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public MonobankTransactionDto setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
