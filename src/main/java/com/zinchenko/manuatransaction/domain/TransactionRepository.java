package com.zinchenko.manuatransaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("""
            select t from Transaction t
            where t.wallet.walletId = :id
            """)
    List<Transaction> findAllByWalletId(Integer id);
}
