package com.zinchenko.monobank.transaction;

import com.zinchenko.admin.category.Category;
import com.zinchenko.monobank.wallet.MonobankWallet;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "monobank_transactions")
public class MonobankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monobank_transaction_id")
    private Integer monobankTransactionId;

    @ManyToOne
    @JoinColumn(name = "monobank_wallet_id", nullable = false)
    private MonobankWallet monobankWallet;

    @Column(name = "amount_in_cents")
    private Long amountInCents;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    public Integer getMonobankTransactionId() {
        return monobankTransactionId;
    }

    public MonobankTransaction setMonobankTransactionId(Integer monobankTransactionId) {
        this.monobankTransactionId = monobankTransactionId;
        return this;
    }

    public MonobankWallet getMonobankWallet() {
        return monobankWallet;
    }

    public MonobankTransaction setMonobankWallet(MonobankWallet monobankWallet) {
        this.monobankWallet = monobankWallet;
        return this;
    }

    public Long getAmountInCents() {
        return amountInCents;
    }

    public MonobankTransaction setAmountInCents(Long amountInCents) {
        this.amountInCents = amountInCents;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public MonobankTransaction setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MonobankTransaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public MonobankTransaction setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
