package com.zinchenko.monobank.domain;

import com.zinchenko.wallet.domain.Wallet;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "monobank_data")
public class MonobankData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monobank_data_id")
    private Integer monobankDataId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "token")
    private String token;

    @Column(name = "last_sync_date")
    private Instant lastSyncDate;

    @OneToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    public Integer getMonobankDataId() {
        return monobankDataId;
    }

    public MonobankData setMonobankDataId(Integer monobankDataId) {
        this.monobankDataId = monobankDataId;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public MonobankData setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public MonobankData setToken(String token) {
        this.token = token;
        return this;
    }

    public Instant getLastSyncDate() {
        return lastSyncDate;
    }

    public MonobankData setLastSyncDate(Instant lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public MonobankData setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}
