package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.AddressDto;
import com.mangoliatrendz.library.model.Address;
import com.mangoliatrendz.library.model.Customer;

public interface AddressService {

    Address save(AddressDto addressDto,String username);

    Address update(AddressDto addressDto);

    AddressDto findById(long id);

    void deleteAddress(long id);

    void enable(long id);

    void disable(long id);

    Address findDefaultAddress(long customer_id);

    Address findByIdOrder(long id);
}
