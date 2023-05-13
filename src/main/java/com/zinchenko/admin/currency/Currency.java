package com.zinchenko.admin.currency;

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

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private Integer code;

    public Currency() {
    }

    public Currency(Integer currencyId, String name) {
        this.currencyId = currencyId;
        this.name = name;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public Currency setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Currency setName(String name) {
        this.name = name;
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
