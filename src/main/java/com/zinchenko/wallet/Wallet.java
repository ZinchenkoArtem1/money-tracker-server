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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "wallet_type")
    private WalletType walletType;

    @Column(name = "balance")
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @OneToMany(mappedBy="wallet")
    private List<Transaction> transactions;

    public Wallet() {
    }

    public Wallet(Integer walletId, User user, String name, WalletType walletType, Double balance, Currency currency) {
        this.walletId = walletId;
        this.user = user;
        this.name = name;
        this.walletType = walletType;
        this.balance = balance;
        this.currency = currency;
    }

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

    public WalletType getWalletType() {
        return walletType;
    }

    public Wallet setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public Double getBalance() {
        return balance;
    }

    public Wallet setBalance(Double balance) {
        this.balance = balance;
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
