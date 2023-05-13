package com.zinchenko.monobank.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatementResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("time")
    private Long time;

    @JsonProperty("description")
    private String description;

    @JsonProperty("mcc")
    private Integer mcc;

    @JsonProperty("originalMcc")
    private Integer originalMcc;

    @JsonProperty("hold")
    private Boolean hold;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("operationAmount")
    private Integer operationAmount;

    @JsonProperty("currencyCode")
    private Integer currencyCode;

    @JsonProperty("commissionRate")
    private Integer commissionRate;

    @JsonProperty("cashbackAmount")
    private Integer cashbackAmount;

    @JsonProperty("balance")
    private Long balance;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("receiptId")
    private String receiptId;

    @JsonProperty("invoiceId")
    private String invoiceId;

    @JsonProperty("counterEdrpou")
    private String counterEdrpou;

    @JsonProperty("counterIban")
    private String counterIban;

    @JsonProperty("counterName")
    private String counterName;

    public String getId() {
        return id;
    }

    public StatementResponse setId(String id) {
        this.id = id;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public StatementResponse setTime(Long time) {
        this.time = time;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StatementResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getMcc() {
        return mcc;
    }

    public StatementResponse setMcc(Integer mcc) {
        this.mcc = mcc;
        return this;
    }

    public Integer getOriginalMcc() {
        return originalMcc;
    }

    public StatementResponse setOriginalMcc(Integer originalMcc) {
        this.originalMcc = originalMcc;
        return this;
    }

    public Boolean getHold() {
        return hold;
    }

    public StatementResponse setHold(Boolean hold) {
        this.hold = hold;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public StatementResponse setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public Integer getOperationAmount() {
        return operationAmount;
    }

    public StatementResponse setOperationAmount(Integer operationAmount) {
        this.operationAmount = operationAmount;
        return this;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public StatementResponse setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Integer getCommissionRate() {
        return commissionRate;
    }

    public StatementResponse setCommissionRate(Integer commissionRate) {
        this.commissionRate = commissionRate;
        return this;
    }

    public Integer getCashbackAmount() {
        return cashbackAmount;
    }

    public StatementResponse setCashbackAmount(Integer cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public StatementResponse setBalance(Long balance) {
        this.balance = balance;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public StatementResponse setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public StatementResponse setReceiptId(String receiptId) {
        this.receiptId = receiptId;
        return this;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public StatementResponse setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public String getCounterEdrpou() {
        return counterEdrpou;
    }

    public StatementResponse setCounterEdrpou(String counterEdrpou) {
        this.counterEdrpou = counterEdrpou;
        return this;
    }

    public String getCounterIban() {
        return counterIban;
    }

    public StatementResponse setCounterIban(String counterIban) {
        this.counterIban = counterIban;
        return this;
    }

    public String getCounterName() {
        return counterName;
    }

    public StatementResponse setCounterName(String counterName) {
        this.counterName = counterName;
        return this;
    }
}
