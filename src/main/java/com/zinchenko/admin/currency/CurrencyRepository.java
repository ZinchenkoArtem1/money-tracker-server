package com.zinchenko.admin.currency;

import com.zinchenko.admin.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findByCode(Integer code);
}
