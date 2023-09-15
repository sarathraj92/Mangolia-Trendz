package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.dto.CustomerDto;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.repository.CustomerRepository;
import com.mangoliatrendz.library.repository.RoleRepository;
import com.mangoliatrendz.library.service.CustomerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;


    private RoleRepository roleRepository;



    public CustomerServiceImpl(CustomerRepository customerRepository,RoleRepository roleRepository) {


        this.roleRepository=roleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customer.set_activated(true);
        customer.setPassword((customerDto.getPassword()));
        customer.setEmail(customerDto.getEmail());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void disable(long id) {
        Customer customer=findById(id);
        customer.set_activated(false);
        customerRepository.save(customer);

    }

    @Override
    public void enable(long id) {
        Customer customer = findById(id);
        customer.set_activated(true);
        customerRepository.save(customer);
    }

    @Override
    public Customer update(CustomerDto customerDto) {
        Customer customer=customerRepository.findByEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());

        return customerRepository.save(customer);
    }

    @Override
    public CustomerDto findByEmailCustomerDto(String email) {
        Customer customer = customerRepository.findByEmail(email);
        CustomerDto customerDto=new CustomerDto();
        customerDto.setEmail(customer.getEmail());
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPassword(customer.getPassword());
        customerDto.set_activated(customer.is_activated());

        return customerDto;
    }

    @Override
    public CustomerDto updateAccount(CustomerDto customerDto,String email) {
        Customer customer= findByEmail(email);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customerRepository.save(customer);
        CustomerDto customerDtoUpdated = convertEntityToDto(customer);
        return customerDtoUpdated;
    }

    @Override
    public void changePass(CustomerDto customerDto) {
        Customer customer=customerRepository.findByEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());

        customerRepository.save(customer);
    }

    public CustomerDto convertEntityToDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.set_activated(customer.is_activated());
        customerDto.setPassword(customer.getPassword());

        return customerDto;
    }


}
