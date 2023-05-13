package com.zinchenko.wallet.monobank.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonobankWalletRepository extends JpaRepository<MonobankWallet, Integer> {

    @Query("""
    select w from MonobankWallet w
    where w.user.email = :email
    """)
    List<MonobankWallet> findAllByUserEmail(String email);
}
