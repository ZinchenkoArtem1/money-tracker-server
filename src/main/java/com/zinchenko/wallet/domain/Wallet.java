package com.zinchenko.wallet.domain;


import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.user.model.User;
import com.zinchenko.transaction.domain.Transaction;
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

    @Column(name = "actual_balance_in_cents")
    private Long actualBalanceInCents;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "wallet_type")
    private WalletType walletType;

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

    public WalletType getWalletType() {
        return walletType;
    }

    public Wallet setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }
}
