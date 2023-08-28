package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.CustomerDto;
import com.mangoliatrendz.library.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer findByEmail(String email);

    Customer save(CustomerDto customerDto);

    List<Customer> findAll();

    Customer findById(long id);

    void disable(long id);

    void enable(long id);

    Customer update(CustomerDto customerDto);

    CustomerDto findByEmailCustomerDto(String email);

    CustomerDto updateAccount(CustomerDto customerDto,String email);

    void changePass(CustomerDto customerDto);
}
