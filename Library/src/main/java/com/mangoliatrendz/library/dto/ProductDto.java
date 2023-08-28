package com.mangoliatrendz.library.dto;

import com.mangoliatrendz.library.model.Category;
import com.mangoliatrendz.library.model.Color;
import com.mangoliatrendz.library.model.Image;
import com.mangoliatrendz.library.model.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String longDescription;
    private String shortDescription;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    private List<Size> sizes;
    private List<Color> colors;

    private List<Image> image;
    private Category category;
    private boolean activated;

}

