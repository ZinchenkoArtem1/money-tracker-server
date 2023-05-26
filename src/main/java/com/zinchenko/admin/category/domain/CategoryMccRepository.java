package com.zinchenko.admin.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryMccRepository extends JpaRepository<CategoryMcc, Integer> {

    Optional<CategoryMcc> findByMcc(Integer mcc);

}
