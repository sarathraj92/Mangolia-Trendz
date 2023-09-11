package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.WalletHistoryDto;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.model.Order;
import com.mangoliatrendz.library.model.Wallet;
import com.mangoliatrendz.library.model.WalletHistory;

import java.util.List;

public interface WalletService {

    List<WalletHistoryDto> findAllById(long id);

    Wallet findByCustomer(Customer customer);

    WalletHistory save(double amount, Customer customer);

    WalletHistory findById(long id);

    void updateWallet(WalletHistory walletHistory,Customer customer,boolean status);

    void debit(Wallet wallet,double totalPrice);

    void returnCredit(Order order,Customer customer);

}
