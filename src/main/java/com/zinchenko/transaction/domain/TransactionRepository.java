package com.zinchenko.transaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("""
            select t from Transaction t
            where t.wallet.walletId = :id
            order by t.createdAt desc
            """)
    List<Transaction> findAllByWalletId(Integer id);

    @Query("""
            select t from Transaction t
            where t.wallet.user.id = :id
            order by t.createdAt desc
            """)
    List<Transaction> findAllByUserId(Integer id);

    @Query("""
            select t from Transaction t
            where t.wallet.walletId = :id
            and t.createdAt >= :from
            and t.createdAt <= :to
            """)
    List<Transaction> findAllByWalletIdInPeriod(Integer id, Instant from, Instant to);

    @Query("""
            select t from Transaction t
            where t.wallet.user.email = :email
            and t.createdAt >= :from
            and t.createdAt <= :to
            """)
    List<Transaction> findAllByUserInPeriod(String email, Instant from, Instant to);

    @Query("""
            select t from Transaction t
            where t.wallet.id = :walletId
            order by createdAt desc
            limit 1
            """)
    Transaction getNewestTransaction(Integer walletId);
}
