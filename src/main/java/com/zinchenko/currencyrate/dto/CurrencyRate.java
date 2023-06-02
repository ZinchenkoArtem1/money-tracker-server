package com.zinchenko.currencyrate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyRate {

    @JsonProperty
    private Integer currencyCodeA;

    @JsonProperty
    private Integer currencyCodeB;

    @JsonProperty
    private Long date;

    @JsonProperty
    private Double rateBuy;

    @JsonProperty
    private Double rateCross;

    @JsonProperty
    private Double rateSell;

    public Integer getCurrencyCodeA() {
        return currencyCodeA;
    }

    public CurrencyRate setCurrencyCodeA(Integer currencyCodeA) {
        this.currencyCodeA = currencyCodeA;
        return this;
    }

    public Integer getCurrencyCodeB() {
        return currencyCodeB;
    }

    public CurrencyRate setCurrencyCodeB(Integer currencyCodeB) {
        this.currencyCodeB = currencyCodeB;
        return this;
    }

    public Long getDate() {
        return date;
    }

    public CurrencyRate setDate(Long date) {
        this.date = date;
        return this;
    }

    public Double getRateBuy() {
        return rateBuy;
    }

    public CurrencyRate setRateBuy(Double rateBuy) {
        this.rateBuy = rateBuy;
        return this;
    }

    public Double getRateCross() {
        return rateCross;
    }

    public CurrencyRate setRateCross(Double rateCross) {
        this.rateCross = rateCross;
        return this;
    }

    public Double getRateSell() {
        return rateSell;
    }

    public CurrencyRate setRateSell(Double rateSell) {
        this.rateSell = rateSell;
        return this;
    }
}

