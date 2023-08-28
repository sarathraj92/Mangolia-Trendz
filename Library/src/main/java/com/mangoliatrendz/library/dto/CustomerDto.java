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
    @Size(min = 3, max = 10, message = "First name contains 3-10 characters")
    @NotEmpty(message = "FirstName should not be empty")
    private String firstName;

    @Size(min = 3, max = 10, message = "Last name contains 3-10 characters")
    @NotEmpty(message = "LastName should not be empty")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;


    @Size(min = 10, max = 15, message = "Phone number contains 10-15 characters")
    @NotEmpty(message = "MobileNumber should not be empty")
    private String mobileNumber;

    @Size(min = 3, max = 15, message = "Password contains 3-10 characters")
    private String password;

    private boolean is_activated;

    private List<Address> address;


    private String confirmPassword;



}
