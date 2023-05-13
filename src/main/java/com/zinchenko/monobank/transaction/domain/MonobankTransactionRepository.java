package com.zinchenko.monobank.transaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonobankTransactionRepository extends JpaRepository<MonobankTransaction, Integer> {

    @Query("""
            select t from MonobankTransaction t
            where t.monobankWallet.monobankWalletId = :id
            """)
    List<MonobankTransaction> findAllByWalletId(Integer id);
}
