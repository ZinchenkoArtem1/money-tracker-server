package com.zinchenko.transaction.domain;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.wallet.domain.Wallet;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount_in_cents")
    private Long amountInCents;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

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

    public Long getAmountInCents() {
        return amountInCents;
    }

    public Transaction setAmountInCents(Long amountInCents) {
        this.amountInCents = amountInCents;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Transaction setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Transaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Transaction setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
