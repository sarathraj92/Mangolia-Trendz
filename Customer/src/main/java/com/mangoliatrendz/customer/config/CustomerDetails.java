package com.mangoliatrendz.customer.config;


import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;


public class CustomerDetails extends org.springframework.security.core.userdetails.User implements Serializable {

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private boolean is_activated;

    public CustomerDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                        String firstName,String lastName, String mobileNumber,boolean is_activated) {
        super(username, password, authorities);
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNumber = mobileNumber;
        this.is_activated= is_activated;
    }
}
