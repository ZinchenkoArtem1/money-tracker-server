package com.zinchenko.monobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientAccountRequest {

    @JsonProperty("token")
    private String token;

    public String getToken() {
        return token;
    }

    public ClientAccountRequest setToken(String token) {
        this.token = token;
        return this;
    }
}
