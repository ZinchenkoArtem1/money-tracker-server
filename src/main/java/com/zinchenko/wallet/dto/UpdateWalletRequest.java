package com.zinchenko.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateWalletRequest {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public UpdateWalletRequest setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UpdateWalletRequest setName(String name) {
        this.name = name;
        return this;
    }
}
