package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.*;
import com.mangoliatrendz.library.repository.*;
import com.mangoliatrendz.library.service.ProductService;
import com.mangoliatrendz.library.utils.ImageUpload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private SizeRepository sizeRepository;
    private ColorRepository colorRepository;

    private CartItemRepository cartItemRepository;

    private OrderDetailRepository orderDetailRepository;

    private ImageUpload imageUpload;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, SizeRepository sizeRepository,
                              ColorRepository colorRepository,
                              ImageUpload imageUpload,ImageRepository imageRepository,CartItemRepository cartItemRepository,
                              OrderDetailRepository orderDetailRepository) {
        this.cartItemRepository=cartItemRepository;
        this.orderDetailRepository=orderDetailRepository;
        this.imageRepository=imageRepository;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository=colorRepository;
        this.imageUpload = imageUpload;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> allProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }
    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setShortDescription(product.getShortDescription());
            productDto.setLongDescription(product.getLongDescription());
            productDto.setBrand(product.getBrand());
            productDto.setSizes(product.getSizes());
            productDto.setSizes(product.getSizes());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public Product save(List<MultipartFile> imageProducts, ProductDto productDto,List<Long> sizes_id,
                        List<Long> colors_id) {
        Product product = new Product();
        try {
            List<Size> sizes=sizeRepository.findAllById(sizes_id);
            List<Color> colors=colorRepository.findAllById(colors_id);
            product.setSizes(sizes);
            product.setColors(colors);
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setShortDescription(productDto.getShortDescription());
            product.setLongDescription(productDto.getLongDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_activated(true);
            Product savedProduct = productRepository.save(product);
            if (imageProducts == null) {
                product.setImage(null);
            } else {
                List<Image> imagesList = new ArrayList<>();
                for (MultipartFile imageProduct : imageProducts) {
                    Image image = new Image();
                    String imageName = imageUpload.storeFile(imageProduct);
                    image.setName(imageName);
                    image.setProduct(product);
                    imageRepository.save(image);
                    imagesList.add(image);
                }
                product.setImage(imagesList);
            }
            return savedProduct;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductDto findById(long id) {
        Product product = productRepository.findById(id);
        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setImage(product.getImage());
        productDto.setName(product.getName());
        productDto.setShortDescription(product.getShortDescription());
        productDto.setLongDescription(product.getLongDescription());
        productDto.setBrand(product.getBrand());
        productDto.setColors(product.getColors());
        productDto.setSizes(product.getSizes());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCategory(product.getCategory());
        productDto.setActivated(product.is_activated());
        return productDto;
    }

    @Override
    public Product update(List<MultipartFile> imageProducts, ProductDto productDto, List<Long> size_id,
                          List<Long> color_id) {
        try {
            long id= productDto.getId();
            Product productUpdate = productRepository.findById(id);
            List<Size> sizes = sizeRepository.findAllById(size_id);
            List<Color> colors = colorRepository.findAllById(color_id);

            productUpdate.setSizes(sizes);
            productUpdate.setColors(colors);
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setName(productDto.getName());
            productUpdate.setBrand(productUpdate.getBrand());
            productUpdate.setShortDescription(productDto.getShortDescription());
            productUpdate.setLongDescription(productDto.getLongDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            if (!imageProducts.isEmpty()) {
                List<Image> imagesList = new ArrayList<>();
                List<Image> image = imageRepository.findImageBy(id);
                int i=0;
                for (MultipartFile imageProduct : imageProducts) {
                    String imageName = imageUpload.storeFile(imageProduct);
                    image.get(i).setName(imageName);
                    image.get(i).setProduct(productUpdate);
                    imageRepository.save(image.get(i));
                    imagesList.add(image.get(i));
                    i++;
                }
                productUpdate.setImage(imagesList);
            }

            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void disable(long id) {
        Product product=productRepository.findById(id);
        product.set_activated(false);

        productRepository.save(product);
    }

    @Override
    public void enable(long id) {
        Product product=productRepository.findById(id);
        product.set_activated(true);

        productRepository.save(product);

    }

    @Override
    public List<ProductDto> findAllByActivated(long id) {
        List<Product> products=productRepository.findAllByActivatedTrue(id);
        List<ProductDto>productDtos = transferData(products);
        return productDtos;
    }
    @Override
    public List<ProductDto> findAllByActivated() {
        List<Product> products=productRepository.findAllByActivatedTrue();
        List<ProductDto>productDtos = transferData(products);
        return productDtos;
    }

    @Override
    public List<ProductDto> findAllByOrderDesc() {
        List<Product> products = productRepository.findAllByOrderById();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id);
        imageRepository.deleteImagesByProductId(id);
        List<Color> colors = product.getColors();
        for(Color color : colors){
            long color_id =color.getId();
            colorRepository.deleteColorsByProductIdAndColorId(id,color_id);
        }
        List<Size> sizes=product.getSizes();
        for(Size size : sizes){
            long size_id = size.getId();
            sizeRepository.deleteSizesByProductIdAndSizeId(id,size_id);
        }
        Set<CartItem> cartItemSet=product.getCartItems();
        for(CartItem cartItem : cartItemSet){
            cartItem.setProduct(null);
            cartItemRepository.save(cartItem);
        }
        List<OrderDetail>orderDetailList=product.getOrderDetails();
        for(OrderDetail orderDetail : orderDetailList){
            orderDetail.setProduct(null);
            orderDetailRepository.save(orderDetail);
        }
        productRepository.delete(product);
    }

    @Override
    public Long countAllProducts() {
        return productRepository.CountAllProducts();
    }

    @Override
    public List<Object[]> getProductStats() {
        return productRepository.getProductStatsForConfirmedOrders();
    }

    @Override
    public List<Object[]> getProductsStatsBetweenDates(Date startDate, Date endDate) {
        return productRepository.getProductsStatsForConfirmedOrdersBetweenDates(startDate,endDate);
    }


}
