package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.Exception.OtpSendException;
import com.mangoliatrendz.library.dto.CustomerDto;
import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.service.CustomerService;
import com.mangoliatrendz.library.service.SmsService;
import com.twilio.Twilio;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {


    private BCryptPasswordEncoder passwordEncoder;

    private CustomerService customerService;

    private SmsService smsService;

    public LoginController(CustomerService customerService,
                           SmsService smsService,BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
        this.smsService=smsService;
        this.customerService=customerService;

    }

    @GetMapping("/login")
    public String getLoginForm(Model model,HttpSession session){
        model.addAttribute("title", "Login Page");
        Object attribute = session.getAttribute("userLoggedIn");
        if(attribute!=null){
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model){

        model.addAttribute("title","Register");
        model.addAttribute("customerDto", new CustomerDto());

        return "register";
    }

    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                   BindingResult result,
                                   Model model, HttpSession httpSession) {



        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            String username = customerDto.getEmail();
            Customer customer = customerService.findByEmail(username);
            if (customer != null) {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("error", "This Email is already Registered!");
                return "register";
            }
           String otp = smsService.generateOtp();
            smsService.sendOtp(otp);
            httpSession.setAttribute("customerDto",customerDto);
            httpSession.setAttribute("otp",otp);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server is error, try again later!");
        }
        return "otp-verify";
    }



    @PostMapping("/do-register/verify")
    public String verifyOtp(HttpSession session,
                            @RequestParam("inputOtp")String inputOtp, Model model){

        CustomerDto customerDto = (CustomerDto) session.getAttribute("customerDto");
        String otp = (String) session.getAttribute("otp");

        if (customerDto != null && otp.equals(inputOtp)) {
            // OTP verified, save user details and login user
            customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerService.save(customerDto);

        } else {
            model.addAttribute("error","OTP is not valid");

            return "otp-verify";
        }

        session.removeAttribute("customerDto");
        session.removeAttribute("otp");


        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String getForgotPassword(){

        return"forgot-password";
    }

    @PostMapping("/send-otp")
    public String resetPassword(@RequestParam("username")String username,
                                RedirectAttributes redirectAttributes,HttpSession session){

        try {

            CustomerDto customerDto = customerService.findByEmailCustomerDto(username);

            session.setAttribute("customerDto", customerDto);

            String otp = smsService.generateOtp();
            smsService.sendOtp(otp);
            session.setAttribute("OTP", otp);
            redirectAttributes.addAttribute("username", username);
        }catch (OtpSendException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }

        return "redirect:/forgot-password";
    }
    @PostMapping("/verify-inputotp")
    public String VerifyOtpPassword(@RequestParam("inputedOtp")String inputOtp,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session){

        String otp=(String)session.getAttribute("OTP");

        if(otp.equals(inputOtp)){
            session.removeAttribute("OTP");
            return "reset-password";
        }else{
            redirectAttributes.addFlashAttribute("error","Otp is invalid");
            return "redirect:/forgot-password";
        }

    }

    @PostMapping("/reset-password")
   public String resetPassword(@RequestParam("password")String password,HttpSession session){

        CustomerDto customerDto=(CustomerDto)session.getAttribute("customerDto");

        customerDto.setPassword(passwordEncoder.encode(password));
        customerService.update(customerDto);
        session.removeAttribute("customerDto");
        return "redirect:/login";
   }





}
