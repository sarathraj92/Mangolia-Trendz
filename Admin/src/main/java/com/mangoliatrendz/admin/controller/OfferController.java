package com.mangoliatrendz.admin.controller;

import com.mangoliatrendz.library.dto.CouponDto;
import com.mangoliatrendz.library.dto.OfferDto;
import com.mangoliatrendz.library.service.OfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OfferController {


    private OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offers")
    public String getCoupon(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        List<OfferDto> offerDtoList=offerService.getAllOffers();
        model.addAttribute("offers",offerDtoList);
        model.addAttribute("size",offerDtoList.size());

        return "offers";
    }


}
