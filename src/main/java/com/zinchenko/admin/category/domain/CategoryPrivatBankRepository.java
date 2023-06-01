package com.zinchenko.admin.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryPrivatBankRepository extends JpaRepository<CategoryPrivatbank, Integer> {

    Optional<CategoryPrivatbank> findByName(String name);
}
