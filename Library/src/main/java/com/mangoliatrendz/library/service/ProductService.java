package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.Color;
import com.mangoliatrendz.library.model.Product;
import com.mangoliatrendz.library.model.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<ProductDto> allProducts();

    Product save(List<MultipartFile> imageProduct, ProductDto product, List<Long> sizes, List<Long> colors);

    ProductDto findById(long id);

    Product update(List<MultipartFile> imageProduct,ProductDto productDto,List<Long> size_id,List<Long>color_id);

    void disable(long id);

    void enable(long id);

    List<ProductDto> findAllByActivated(long id);
    List<ProductDto> findAllByActivated();

    List<ProductDto> findAllByOrderDesc();

    void deleteProduct(long id);

    Long countAllProducts();


    List<Object[]> getProductStats();

    List<Object[]> getProductsStatsBetweenDates(Date startDate,Date endDate);

    Product findBYId(long id);

    List<Product> findProductsByCategory(long id);



}
