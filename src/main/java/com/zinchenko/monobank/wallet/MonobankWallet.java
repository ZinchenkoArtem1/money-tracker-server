package com.zinchenko.monobank.wallet;

import com.zinchenko.admin.currency.Currency;
import com.zinchenko.monobank.transaction.MonobankTransaction;
import com.zinchenko.user.model.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "monobank_wallets")
public class MonobankWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monobank_wallet_id")
    private Integer monobankWalletId;

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

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "token")
    private String token;

    @OneToMany(mappedBy = "monobankWallet")
    private List<MonobankTransaction> transactions;

    @Column(name = "last_sync_date")
    private Instant lastSyncDate;

    public Integer getMonobankWalletId() {
        return monobankWalletId;
    }

    public MonobankWallet setMonobankWalletId(Integer monobankWalletId) {
        this.monobankWalletId = monobankWalletId;
        return this;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public MonobankWallet setUser(User user) {
//        this.user = user;
//        return this;
//    }

    public String getName() {
        return name;
    }

    public MonobankWallet setName(String name) {
        this.name = name;
        return this;
    }

    public Long getActualBalanceInCents() {
        return actualBalanceInCents;
    }

    public MonobankWallet setActualBalanceInCents(Long actualBalanceInCents) {
        this.actualBalanceInCents = actualBalanceInCents;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public MonobankWallet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public MonobankWallet setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public MonobankWallet setToken(String token) {
        this.token = token;
        return this;
    }

    public List<MonobankTransaction> getTransactions() {
        return transactions;
    }

    public MonobankWallet setTransactions(List<MonobankTransaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Instant getLastSyncDate() {
        return lastSyncDate;
    }

    public MonobankWallet setLastSyncDate(Instant lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public MonobankWallet setUser(User user) {
        this.user = user;
        return this;
    }
}
