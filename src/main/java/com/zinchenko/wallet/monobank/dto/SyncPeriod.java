package com.zinchenko.wallet.monobank.dto;

import java.time.temporal.ChronoUnit;

public enum SyncPeriod {

    ONE_DAY(ChronoUnit.DAYS, 1),
    TEN_DAYS(ChronoUnit.DAYS, 10),
    THIRTY_DAYS(ChronoUnit.DAYS, 30);

    private final ChronoUnit unit;
    private final int count;

    SyncPeriod(ChronoUnit unit, int count) {
        this.unit = unit;
        this.count = count;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public int getCount() {
        return count;
    }
}
