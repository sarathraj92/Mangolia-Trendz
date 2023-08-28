package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.Category;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.service.CategoryService;
import com.mangoliatrendz.library.service.CustomerService;
import com.mangoliatrendz.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private CategoryService categoryService;

    private ProductService productService;

    private CustomerService customerService;

    public ProductController(CategoryService categoryService, ProductService productService,
                             CustomerService customerService) {
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
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);


        return "index";
    }

    @GetMapping("/products-list/{id}")
    public String getShopPage(@PathVariable("id") long id,Model model){
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products =productService.findAllByActivated(id);
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);

        return "shop";
    }
    @GetMapping("/products-list")
    public String getShopPage(Model model){
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products =productService.findAllByActivated();
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);

        return "shop";
    }


    @GetMapping("/product-full/{id}")
    public String getProductFull(@PathVariable("id")long id,Model model){
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto=productService.findById(id);
        model.addAttribute("categories",categories);
        model.addAttribute("productDto",productDto);
        return "/product-full";
    }


}
