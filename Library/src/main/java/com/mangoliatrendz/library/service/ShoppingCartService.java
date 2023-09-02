package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.ShoppingCart;
import com.mangoliatrendz.library.model.Size;

import java.util.List;

public interface ShoppingCartService {


    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username, Long size);

    ShoppingCart updateCart(ProductDto productDto, int quantity, String username,Long cart_Item_Id);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);

    ShoppingCart updateTotalPrice(Double newTotalPrice,String username);

    void deleteCartById(long id);
}
