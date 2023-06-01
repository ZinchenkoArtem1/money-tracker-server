package com.zinchenko.common.money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MoneyConvertorTest {

    private MoneyConvertor moneyConvertor;

    @BeforeEach
    void setUp() {
        moneyConvertor = new MoneyConvertor();
    }

    @Test
    void toCentsFromDoubleTest() {
        Double units = 1.50D;

        Long cents = moneyConvertor.toCents(units);

        assertEquals(150L, cents);
    }

    @Test
    void toCentsFromStringTest() {
        String units = "1.50";

        Long cents = moneyConvertor.toCents(units);

        assertEquals(150L, cents);
    }

    @Test
    void toUnitsTest() {
        Long cents = 150L;

        Double units = moneyConvertor.toUnits(cents);

        assertEquals(1.50D, units);
    }
}
