package com.mangoliatrendz.admin.config;




import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;

public class AdminDetails extends org.springframework.security.core.userdetails.User {


    private String firstName;

    private String lastName;

    private String mobileNumber;

    public AdminDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                               String firstName,String lastName, String mobileNumber) {
        super(username, password, authorities);
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNumber = mobileNumber;
    }
}
