package com.zinchenko.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "registry_type")
    private String registryType;

    @OneToMany(mappedBy="user")
    private List<Wallet> wallets;

    public User() {
    }

    public User(Integer userId, String username, String registryType) {
        this.userId = userId;
        this.username = username;
        this.registryType = registryType;
    }

    public Integer getUserId() {
        return userId;
    }

    public User setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRegistryType() {
        return registryType;
    }

    public User setRegistryType(String registryType) {
        this.registryType = registryType;
        return this;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public User setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
        return this;
    }
}
