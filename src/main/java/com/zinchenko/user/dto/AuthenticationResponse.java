package com.zinchenko.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("token")
    private String token;

    public String getEmail() {
        return email;
    }

    public AuthenticationResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getToken() {
        return token;
    }

    public AuthenticationResponse setToken(String token) {
        this.token = token;
        return this;
    }
}
