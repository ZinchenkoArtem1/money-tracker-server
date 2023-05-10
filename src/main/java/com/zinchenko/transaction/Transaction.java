package com.zinchenko.transaction;

import com.zinchenko.admin.category.Category;
import com.zinchenko.wallet.Wallet;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "payee")
    private String payee;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Date date;

    public Transaction() {
    }

    public Transaction(Integer transactionId, Wallet wallet, Double amount, Category category, String payee, String description, Date date) {
        this.transactionId = transactionId;
        this.wallet = wallet;
        this.amount = amount;
        this.category = category;
        this.payee = payee;
        this.description = description;
        this.date = date;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Transaction setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Transaction setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getPayee() {
        return payee;
    }

    public Transaction setPayee(String payee) {
        this.payee = payee;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Transaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Transaction setDate(Date date) {
        this.date = date;
        return this;
    }
}
