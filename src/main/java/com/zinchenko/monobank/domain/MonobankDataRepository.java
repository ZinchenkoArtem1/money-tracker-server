package com.zinchenko.monobank.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonobankDataRepository extends JpaRepository<MonobankData, Integer> {

    @Query("""
            select m from MonobankData m
            where m.wallet.walletId = :walletId
            """)
    Optional<MonobankData> findByWalletId(Integer walletId);
}
