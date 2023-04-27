package com.zinchenko.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet_types")
public class WalletType {

    @Id
    @Column(name = "wallet_type_id")
    private Integer walletTypeId;

    @Column(name = "name")
    private String name;

    public WalletType() {
    }

    public WalletType(Integer walletTypeId, String name) {
        this.walletTypeId = walletTypeId;
        this.name = name;
    }

    public Integer getWalletTypeId() {
        return walletTypeId;
    }

    public WalletType setWalletTypeId(Integer walletTypeId) {
        this.walletTypeId = walletTypeId;
        return this;
    }

    public String getName() {
        return name;
    }

    public WalletType setName(String name) {
        this.name = name;
        return this;
    }
}
