package com.zinchenko.admin.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryMonobankRepository extends JpaRepository<CategoryMonobank, Integer> {

    Optional<CategoryMonobank> findByMcc(Integer mcc);

}
