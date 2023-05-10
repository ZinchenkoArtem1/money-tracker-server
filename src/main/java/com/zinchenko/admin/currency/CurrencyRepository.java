package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
