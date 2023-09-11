package com.mangoliatrendz.library.repository;

import com.mangoliatrendz.library.model.Wallet;
import com.mangoliatrendz.library.model.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {



}
