package com.zinchenko.admin.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private Integer code;

    public Integer getId() {
        return id;
    }

    public CurrencyDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CurrencyDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public CurrencyDto setCode(Integer code) {
        this.code = code;
        return this;
    }
}
