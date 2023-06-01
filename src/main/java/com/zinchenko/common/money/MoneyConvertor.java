package com.zinchenko.common.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyConvertor {

    public Double toUnits(Long cents) {
        return BigDecimal.valueOf(cents).movePointLeft(2).doubleValue();
    }

    public Long toCents(Double units) {
        return BigDecimal.valueOf(units).movePointRight(2).longValue();
    }

    public Long toCents(String units) {
        return new BigDecimal(units).movePointRight(2).longValue();
    }
}
