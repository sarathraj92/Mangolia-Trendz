package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.dto.BannerDto;
import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.Category;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.service.BannerService;
import com.mangoliatrendz.library.service.CategoryService;
import com.mangoliatrendz.library.service.CustomerService;
import com.mangoliatrendz.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private CategoryService categoryService;

    private ProductService productService;

    private CustomerService customerService;

    private BannerService bannerService;

    public ProductController(CategoryService categoryService, ProductService productService,
                             CustomerService customerService,BannerService bannerService) {
        this.bannerService=bannerService;
        this.customerService=customerService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal, HttpSession session){
        if (principal != null) {
            Customer customer = customerService.findByEmail(principal.getName());
            session.setAttribute("userLoggedIn",true);
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());
        }
        List<BannerDto> bannerDtoList=bannerService.getAllBanners();
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products=productService.findAllProducts();
        model.addAttribute("categories",categories);
        model.addAttribute("banners",bannerDtoList);
        model.addAttribute("products",products);


        return "index";
    }

    @GetMapping("/products-list")
    public String getShopPage(@RequestParam(name = "id",required = false,defaultValue = "0") long id, Model model,
                              @RequestParam(name = "pageNo",required = false,defaultValue = "0")int pageNo,
                              @RequestParam(name = "sort",required = false,defaultValue = "")String sort){
        List<Category> categories = categoryService.findAllByActivatedTrue();

        Page<ProductDto> products;
        if(id==0) {
            products = productService.findAllByActivated(pageNo,sort);
            model.addAttribute("sort",sort);
        }else{
            products = productService.findAllByActivated(id,pageNo);
        }
        long totalProducts = products.getTotalElements();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("products",products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size",products.getSize());
        model.addAttribute("categories",categories);


        return "shop";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(name = "keyword") String keyword,
                                Model model
    ) {

        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        long totalProducts = products.getTotalElements();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "shop";

    }


    @GetMapping("/product-full/{id}")
    public String getProductFull(@PathVariable("id")long id,Model model){
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto=productService.findById(id);
        model.addAttribute("categories",categories);
        model.addAttribute("productDto",productDto);
        return "product-full";
    }


}
