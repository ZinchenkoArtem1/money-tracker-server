package com.zinchenko.wallet.monobank.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface MonobankTransactionRepository extends JpaRepository<MonobankTransaction, Integer> {

    @Query("""
            delete from MonobankTransaction tx
            where tx.monobankWallet.monobankWalletId = :walletId
            and tx.createdAt >= :from and tx.createdAt <= :to
            """)
    void clearTransactionForPeriod(Long from, Long to, Integer walletId);
}
