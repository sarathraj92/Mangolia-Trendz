package com.mangoliatrendz.customer.config;

import com.mangoliatrendz.customer.exception.CustomerBlockedException;


import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.model.Role;
import com.mangoliatrendz.library.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDetailService implements UserDetailsService {

    private CustomerRepository customerRepository;


    public CustomerDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

            if(customer !=null) {
                if (customer.is_activated()) {

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    for (Role role : customer.getRoles()) {
                        authorities.add(new SimpleGrantedAuthority(role.getName()));
                    }

                    return new CustomerDetails(
                            customer.getEmail(),
                            customer.getPassword(),
                            authorities,
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getMobileNumber(),
                            customer.is_activated());


                } else {

                    throw new CustomerBlockedException("Your account has been blocked. Please contact support.");
                }
            }else{
                throw new UsernameNotFoundException("Invalid username or password.");
            }

    }
}
