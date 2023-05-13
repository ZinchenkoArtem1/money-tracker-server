package com.zinchenko.manualwallet.domain;


import com.zinchenko.admin.currency.Currency;
import com.zinchenko.user.model.User;
import com.zinchenko.manuatransaction.Transaction;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Integer walletId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "initial_balance_in_cents")
    private Long initialBalanceInCents;

    @Column(name = "actual_balance_in_cents")
    private Long actualBalanceInCents;

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

    public Long getInitialBalanceInCents() {
        return initialBalanceInCents;
    }

    public Wallet setInitialBalanceInCents(Long initialBalanceInCents) {
        this.initialBalanceInCents = initialBalanceInCents;
        return this;
    }

    public Long getActualBalanceInCents() {
        return actualBalanceInCents;
    }

    public Wallet setActualBalanceInCents(Long actualBalanceInCents) {
        this.actualBalanceInCents = actualBalanceInCents;
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
