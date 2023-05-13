package com.zinchenko.wallet.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query("""
    select w from Wallet w
    where w.user.email = :email
    """)
    List<Wallet> findByUserEmail(@Param("email") String email);
}
