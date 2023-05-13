package com.zinchenko.monobank.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientAccountResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("masked_pan")
    private String maskedPan;

    public String getId() {
        return id;
    }

    public ClientAccountResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public ClientAccountResponse setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
        return this;
    }
}
