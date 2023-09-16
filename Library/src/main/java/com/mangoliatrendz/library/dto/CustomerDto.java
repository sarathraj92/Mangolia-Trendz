package com.mangoliatrendz.library.dto;


import com.mangoliatrendz.library.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

    private long id;

    private String firstName;


    private String lastName;


    @Email
    private String email;



    private String mobileNumber;

    
    private String password;

    private boolean is_activated;

    private List<Address> address;


    private String confirmPassword;



}
