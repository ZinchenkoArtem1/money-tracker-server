package com.zinchenko.admin.currency.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findByCode(Integer code);

    Optional<Currency> findByNameUkr(String nameUkr);
}
