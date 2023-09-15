package com.mangoliatrendz.admin.controller;

import com.mangoliatrendz.library.dto.CategoryDto;
import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.*;
import com.mangoliatrendz.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;
    private ImageService imageService;

    private SizeService sizeService;

    private ColorService colorService;


    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService,
                             SizeService sizeService, ImageService imageService,ColorService colorService) {
        this.colorService=colorService;
        this.sizeService=sizeService;
        this.imageService=imageService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String productList(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<ProductDto> products = productService.findAllByOrderDesc();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());

        return "products";
    }

    @GetMapping("/add-product")
    public String addProductPage(Model model) {
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Size> sizes= sizeService.allSizeVariations();
        List<Color> colors = colorService.allColorVariations();
        model.addAttribute("categories", categories);
        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categoryNew", new CategoryDto());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
                              @RequestParam("imageProduct") List<MultipartFile> imageProduct,
                              @RequestParam("sizes") List<Long> sizes_id,
                              @RequestParam("colors") List<Long> colors_id,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.save(imageProduct, product,sizes_id,colors_id);
            redirectAttributes.addFlashAttribute("success", "Added new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return "redirect:/products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<Size> sizes = sizeService.allSizeVariations();
        List<Color> colors = colorService.allColorVariations();
        ProductDto productDto = productService.findById(id);
        List<Image> images =imageService.findProductImages(id);
        model.addAttribute("title", "Update Product");
        model.addAttribute("categories", categories);
        model.addAttribute("images", images);
        model.addAttribute("sizes",sizes);
        model.addAttribute("colors",colors);

        model.addAttribute("productDto", productDto);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") List<MultipartFile> imageProduct,
                                @RequestParam("sizes") List<Long> sizes_id,
                                @RequestParam("colors") List<Long> colors_id,
                                RedirectAttributes redirectAttributes) {
        try {

            productService.update(imageProduct, productDto,sizes_id,colors_id);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/products";
    }

    @GetMapping("/disable-product/{id}")
    public String disable(@PathVariable("id")long id,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success","Product Disabled");

        productService.disable(id);
        return "redirect:/products";
    }

    @GetMapping("/enable-product/{id}")
    public String enable(@PathVariable("id")long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success","Product Enabled");

        productService.enable(id);
        return "redirect:/products";
    }

    @GetMapping("/delete-product/{id}")
    public String delete(@PathVariable("id")long id,RedirectAttributes redirectAttributes){

        productService.deleteProduct(id);

        redirectAttributes.addFlashAttribute("success","Product deleted");

        return "redirect:/products";
    }





}
