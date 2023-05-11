package com.zinchenko.wallet;


import com.zinchenko.admin.currency.Currency;
import com.zinchenko.security.model.User;
import com.zinchenko.transaction.Transaction;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    private Integer walletId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "initial_balance")
    private Double initialBalance;

    @Column(name = "actual_balance")
    private Double actualBalance;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;

    public Integer getWalletId() {
        return walletId;
    }

    public Wallet setWalletId(Integer walletId) {
        this.walletId = walletId;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Wallet setUser(User user) {
        this.user = user;
        return this;
    }

    public String getName() {
        return name;
    }

    public Wallet setName(String name) {
        this.name = name;
        return this;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public Wallet setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
        return this;
    }

    public Double getActualBalance() {
        return actualBalance;
    }

    public Wallet setActualBalance(Double actualBalance) {
        this.actualBalance = actualBalance;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Wallet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Wallet setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }
}
