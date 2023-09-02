package com.mangoliatrendz.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    private Long id;

    private String name;

    private String description;


    private int offPercentage;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;


    private String offerType;

    private Long applicableForId;

    private String applicableForName;

    private boolean enabled;


}
