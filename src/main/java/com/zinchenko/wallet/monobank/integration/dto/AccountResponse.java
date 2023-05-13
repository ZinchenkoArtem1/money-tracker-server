package com.zinchenko.wallet.monobank.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AccountResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("sendId")
    private String sendId;

    @JsonProperty("balance")
    private Integer balance;

    @JsonProperty("creditLimit")
    private Integer creditLimit;

    @JsonProperty("type")
    private String type;

    @JsonProperty("currencyCode")
    private Integer currencyCode;

    @JsonProperty("cashbackType")
    private String cashbackType;

    @JsonProperty("maskedPan")
    private List<String> maskedPan;

    @JsonProperty("iban")
    private String iban;

    public String getId() {
        return id;
    }

    public AccountResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getSendId() {
        return sendId;
    }

    public AccountResponse setSendId(String sendId) {
        this.sendId = sendId;
        return this;
    }

    public Integer getBalance() {
        return balance;
    }

    public AccountResponse setBalance(Integer balance) {
        this.balance = balance;
        return this;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public AccountResponse setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
        return this;
    }

    public String getType() {
        return type;
    }

    public AccountResponse setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public AccountResponse setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public String getCashbackType() {
        return cashbackType;
    }

    public AccountResponse setCashbackType(String cashbackType) {
        this.cashbackType = cashbackType;
        return this;
    }

    public List<String> getMaskedPan() {
        return maskedPan;
    }

    public AccountResponse setMaskedPan(List<String> maskedPan) {
        this.maskedPan = maskedPan;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public AccountResponse setIban(String iban) {
        this.iban = iban;
        return this;
    }
}
