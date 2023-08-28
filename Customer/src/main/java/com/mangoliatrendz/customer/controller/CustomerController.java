package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.dto.AddressDto;
import com.mangoliatrendz.library.dto.CustomerDto;
import com.mangoliatrendz.library.model.Address;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.service.AddressService;
import com.mangoliatrendz.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Spliterators;

@Controller
public class CustomerController {

    private CustomerService customerService;

    private AddressService addressService;

    private BCryptPasswordEncoder passwordEncoder;

    public CustomerController(CustomerService customerService,AddressService addressService,
                              BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
        this.addressService=addressService;
        this.customerService = customerService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(@RequestParam(required = false) String tab, Model model, Principal principal,HttpSession session){
        if(principal==null){
            return "redirect:/login";
        }else{
            Customer customer = customerService.findByEmail(principal.getName());
            session.setAttribute("userLoggedIn",true);
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());
            if(tab!=null && !tab.isEmpty()) {
                model.addAttribute("openTab", tab);
                System.out.println(tab);
            }else{
                model.addAttribute("openTab", "");
            }
            model.addAttribute("customer",customer);
            model.addAttribute("title","Dashboard");
            return "dashboard";
        }
    }

    @GetMapping("/about")
    public String getAboutUs(){
        return "about";
    }
    @GetMapping("/contact")
    public String getContactUs(){
        return "contact";
    }


    @GetMapping("/address")
    public String getAddress(Principal principal,
                             Model model){
        if(principal==null){
            return "redirect:/login";
        }else{
            Customer customer = customerService.findByEmail(principal.getName());
            List<Address> address = customer.getAddress();
            model.addAttribute("addressList",address);
            model.addAttribute("size",address.size());
            model.addAttribute("addressDto",new AddressDto());

            return "address-content";
        }
    }

    @PostMapping("/add-address")
    public String getAddAddress(@Valid @ModelAttribute("addressDto")AddressDto addressDto,
                                BindingResult result,Model model,Principal principal){

        if (result.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "redirect:/dashboard?tab=address";
        }
          addressService.save(addressDto, principal.getName());
          model.addAttribute("success","Address Added");

            return "redirect:/dashboard?tab=address";
        }


        @GetMapping("/update-address/{id}")
        public String getUpdateAddress(@PathVariable("id")Long address_Id,Model model,Principal principal){
          if(principal==null){
              return "redirect:/login";
          }
          AddressDto addressDto=addressService.findById(address_Id);
          model.addAttribute("addressDto",addressDto);

            return"update-address";
        }

        @PostMapping("/update-address/{id}")
        public String updateAddress(@Valid@ModelAttribute("addressDto")AddressDto addressDto,Model model,
                                    BindingResult result,HttpServletRequest request){
            if (result.hasErrors()) {
                model.addAttribute("addressDto", addressDto);
                return "update-address";
            }
            addressService.update(addressDto);
            model.addAttribute("success","Address updated");

            return "redirect:/dashboard?tab=address";

        }

        @GetMapping("/delete-address/{id}")
        public String deleteAddress(@PathVariable("id")Long address_id,Model model,HttpServletRequest request){

            addressService.deleteAddress(address_id);
            model.addAttribute("success","Address Deleted");


            return "redirect:/dashboard?tab=address";
        }

        @GetMapping("/enable-address/{id}")
        public String enableAddress(@PathVariable("id")long address_id,
                                    RedirectAttributes redirectAttributes,HttpServletRequest request){

            addressService.enable(address_id);
            redirectAttributes.addFlashAttribute("success","Address enabled");

            return "redirect:/dashboard?tab=address";
        }

       @GetMapping("/disable-address/{id}")
       public String disableAddress(@PathVariable("id")long address_id,
                                 RedirectAttributes redirectAttributes,HttpServletRequest request){

        addressService.disable(address_id);
        redirectAttributes.addFlashAttribute("success","Address disabled");

        return "redirect:/dashboard?tab=address";
       }

       @GetMapping("/account-details")
       public String getUpdateAccount(Principal principal,Model model){

        if(principal==null){
            return "redirect:/login";
        }else{
            CustomerDto customer = customerService.findByEmailCustomerDto(principal.getName());
            model.addAttribute("customer",customer);
            return "account-details";
        }
       }

       @PostMapping("/update-account")
       public String UpdateAccount(@ModelAttribute("customer")CustomerDto customerDto,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal){

        if(principal==null){
            return "redirect:/login";
        }else{

            CustomerDto customerUpdated = customerService.updateAccount(customerDto,principal.getName());
            redirectAttributes.addFlashAttribute("customer",customerUpdated);
            redirectAttributes.addFlashAttribute("success","Updated Successfully");
            return "redirect:/dashboard?tab=account-detail";

        }
       }


    @PostMapping("/change-password")
    public String changePass(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {

            CustomerDto customer = customerService.findByEmailCustomerDto(principal.getName());
            if (passwordEncoder.matches(oldPassword, customer.getPassword())
                    && !passwordEncoder.matches(newPassword, oldPassword)
                    && !passwordEncoder.matches(newPassword, customer.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 3) {

                customer.setPassword(passwordEncoder.encode(newPassword));
                customerService.changePass(customer);
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "redirect:/dashboard?tab=account-detail";
            } else {

                attributes.addFlashAttribute("message", "Entered Password Does Not Match");
                return "redirect:/dashboard?tab=account-detail";
            }
        }
    }




    }


