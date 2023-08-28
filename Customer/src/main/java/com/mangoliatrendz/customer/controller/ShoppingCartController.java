package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.dto.ProductDto;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.model.ShoppingCart;
import com.mangoliatrendz.library.model.Size;
import com.mangoliatrendz.library.service.CustomerService;
import com.mangoliatrendz.library.service.ProductService;
import com.mangoliatrendz.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ShoppingCartController {


    private ShoppingCartService shoppingCartService;

    private ProductService productService;

    private CustomerService customerService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService,
                                  CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/cart")
    public String getCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            ShoppingCart cart = customer.getCart();
            if (cart==null) {
                model.addAttribute("check","You don't have any items in your cart");

            }
            if (cart != null) {
                model.addAttribute("grandTotal", cart.getTotalPrice());
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");


            return "cart";
        }
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("productId") Long id,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("selectedSizeId") long sizeId,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {


        ProductDto productDto = productService.findById(id);
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            ShoppingCart shoppingCart = shoppingCartService.addItemToCart(productDto, quantity, username,sizeId);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("cart_item_id")Long cart_item_id,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productService.findById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = shoppingCartService.updateCart(productDto, quantity, username,cart_item_id);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/cart";
        }

    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productService.findById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = shoppingCartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/cart";
        }
    }




}

