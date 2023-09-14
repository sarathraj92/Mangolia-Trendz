package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.model.Product;
import com.mangoliatrendz.library.model.Wishlist;
import com.mangoliatrendz.library.repository.WishlistRepository;
import com.mangoliatrendz.library.service.ProductService;
import com.mangoliatrendz.library.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {


    private WishlistRepository wishlistRepository;

    private ProductService productService;

    public WishlistServiceImpl(WishlistRepository wishlistRepository,ProductService productService) {
        this.productService=productService;
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<Wishlist> findAllByCustomer(Customer customer) {

        List<Wishlist>Wishlists=wishlistRepository.findAllByCustomerId(customer.getId());


        return Wishlists;
    }

    @Override
    public boolean findByProductId(long productId,Customer customer) {

        boolean exists= wishlistRepository.findByProductIdAndCustomerId(productId,customer.getId());


        return exists;
    }

    @Override
    public Wishlist save(long productId, Customer customer) {

        Product product=productService.findBYId(productId);
        Wishlist wishlist=new Wishlist();
        wishlist.setProduct(product);
        wishlist.setCustomer(customer);
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    @Override
    public void deleteWishlist(long id) {
        Wishlist wishlist=wishlistRepository.findById(id);
        wishlistRepository.delete(wishlist);
    }
}
