package com.zinchenko.admin.currency;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

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
}
