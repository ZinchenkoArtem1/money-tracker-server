package com.zinchenko.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class TransactionDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("amount_in_units")
    private Double amountInUnits;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("wallet_id")
    private Integer walletId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("currency_name")
    private String currencyName;

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

    public String getCategoryName() {
        return categoryName;
    }

    public TransactionDto setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getCurrencyName() {
        return currencyName;
    }

    public TransactionDto setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }
}
