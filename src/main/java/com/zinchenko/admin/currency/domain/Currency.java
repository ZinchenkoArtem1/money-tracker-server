package com.zinchenko.admin.currency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "name_eng")
    private String nameEng;

    @Column(name = "name_ukr")
    private String nameUkr;

    @Column(name = "code")
    private Integer code;

    public Currency() {
    }

    public Currency(Integer currencyId, String name) {
        this.currencyId = currencyId;
        this.nameEng = name;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public Currency setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public String getNameEng() {
        return nameEng;
    }

    public Currency setNameEng(String nameEng) {
        this.nameEng = nameEng;
        return this;
    }

    public String getNameUkr() {
        return nameUkr;
    }

    public Currency setNameUkr(String nameUkr) {
        this.nameUkr = nameUkr;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public Currency setCode(Integer code) {
        this.code = code;
        return this;
    }
}
