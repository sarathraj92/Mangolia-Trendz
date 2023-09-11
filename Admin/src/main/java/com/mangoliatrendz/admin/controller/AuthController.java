package com.mangoliatrendz.admin.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AuthController {





    @GetMapping("/login")
    public String getLoginForm(HttpSession session) {
        Object attribute = session.getAttribute("userLoggedIn");
        if (attribute != null) {
            return "redirect:/";
        }


        return "login";
    }

}

