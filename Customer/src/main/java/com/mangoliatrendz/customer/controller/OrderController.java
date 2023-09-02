package com.mangoliatrendz.customer.controller;

import com.mangoliatrendz.library.dto.AddressDto;
import com.mangoliatrendz.library.model.*;
import com.mangoliatrendz.library.service.*;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
public class OrderController {

    private CustomerService customerService;
    private OrderService orderService;
    private ShoppingCartService shoppingCartService;
    private AddressService addressService;

    private CouponService couponService;


    public OrderController(CustomerService customerService, OrderService orderService, ShoppingCartService shoppingCartService,
                           AddressService addressService,CouponService couponService) {

        this.couponService=couponService;
        this.addressService=addressService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByEmail(principal.getName());

                ShoppingCart cart = customerService.findByEmail(principal.getName()).getCart();
                Set<CartItem> cartItems=cart.getCartItems();
                List<Address> addressList = customer.getAddress();
                model.addAttribute("addressDto",new AddressDto());
                model.addAttribute("customer", customer);
                model.addAttribute("addressList", addressList);
                model.addAttribute("size",addressList.size());
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("page", "Check-Out");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }

    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=apply")
        public String applyCoupon(@RequestParam("couponCode")String couponCode,Principal principal,
                                  RedirectAttributes attributes,HttpSession session){


            if(couponService.findByCouponCode(couponCode.toUpperCase())) {

                Coupon coupon = couponService.findByCode(couponCode.toUpperCase());
                ShoppingCart cart = customerService.findByEmail(principal.getName()).getCart();
                Double totalPrice = cart.getTotalPrice();
                session.setAttribute("totalPrice",totalPrice);
                Double newTotalPrice = couponService.applyCoupon(couponCode.toUpperCase(), totalPrice);

                shoppingCartService.updateTotalPrice(newTotalPrice, principal.getName());

                attributes.addFlashAttribute("success", "Coupon applied Successfully");
                attributes.addAttribute("couponCode", couponCode);
                attributes.addAttribute("couponOff", coupon.getOffPercentage());
                return "redirect:/check-out";
            }else{
                attributes.addFlashAttribute("error", "Coupon Code invalid");
                return "redirect:/check-out";
            }

     }

    @RequestMapping(value = "/check-out/apply-coupon", method = RequestMethod.POST, params = "action=remove")
    public String removeCoupon(Principal principal,RedirectAttributes attributes,
                               HttpSession session){

        Double totalPrice = (Double) session.getAttribute("totalPrice");
        shoppingCartService.updateTotalPrice(totalPrice, principal.getName());
        attributes.addFlashAttribute("success", "Coupon removed Successfully");
//        session.removeAttribute("totalPrice");

        return "redirect:/check-out";
    }



    @RequestMapping(value = "/add-order",method = RequestMethod.POST)
    @ResponseBody
    public String createOrder(@RequestBody Map<String,Object> data, Principal principal, HttpSession session, Model model) throws RazorpayException {
            String paymentMethod = data.get("payment_Method").toString();
            Long address_id=Long.parseLong(data.get("addressId").toString());
            Double oldTotalPrice= (Double)session.getAttribute("totalPrice");
        Customer customer = customerService.findByEmail(principal.getName());
        ShoppingCart cart = customer.getCart();
            if(paymentMethod.equals("COD")){
            Order order = orderService.save(cart,address_id,paymentMethod,oldTotalPrice);
            session.removeAttribute("totalItems");
            session.setAttribute("orderId",order.getId());
            model.addAttribute("order", order);
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Order Added Successfully");
            JSONObject options = new JSONObject();
            options.put("status","Cash");

            return options.toString();
        }else{
                Order order = orderService.save(cart,address_id,paymentMethod,oldTotalPrice);
                String orderId=order.getId().toString();
                session.removeAttribute("totalItems");
                session.setAttribute("orderId",order.getId());
                model.addAttribute("order", order);
                model.addAttribute("page", "Order Detail");
                model.addAttribute("success", "Order Added Successfully");
                RazorpayClient razorpayClient=new RazorpayClient("rzp_test_R6cOmfR30Lcubq","nNZOZQUWSrxXLRZ5C9wvOmfQ");
                JSONObject options = new JSONObject();
                options.put("amount",order.getTotalPrice()*100);
                options.put("currency","INR");
                options.put("receipt",orderId);
                com.razorpay.Order orderRazorPay = razorpayClient.orders.create(options);
                return orderRazorPay.toString();
            }

    }

    @RequestMapping(value = "/verify-payment",method = RequestMethod.POST)
    @ResponseBody
    public String verifyPayment(@RequestBody Map<String,Object> data,HttpSession session) throws RazorpayException {
        System.out.println(data);
        String secret= "nNZOZQUWSrxXLRZ5C9wvOmfQ";
        String order_id= data.get("razorpay_order_id").toString();
        String payment_id=data.get("razorpay_payment_id").toString();
        String signature=data.get("razorpay_signature").toString();
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", order_id);
        options.put("razorpay_payment_id", payment_id);
        options.put("razorpay_signature", signature);

        boolean status =  Utils.verifyPaymentSignature(options, secret);
        JSONObject response = new JSONObject();
        response.put("status",status);

        return response.toString();
    }


    @GetMapping("/order-confirmation")
    public String getOrderConfirmation(Model model,HttpSession session){

        Long order_id=(Long)session.getAttribute("orderId");
        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }else{
            model.addAttribute("payment","Successful");
        }
        model.addAttribute("order", order);
        model.addAttribute("success", "Order Added Successfully");
        session.removeAttribute("orderId");

        return "order-detail";
    }


    @GetMapping("/orders")
    public String getOrder(Principal principal,Model model){
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            List<Order> orderList = orderService.findAllOrdersByCustomer(customer.getId());
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            return "orders";
        }
    }

    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id")long order_id, RedirectAttributes attributes){
        orderService.cancelOrder(order_id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/dashboard?tab=orders";
    }


    @GetMapping("/order-view/{id}")
    public String orderView(@PathVariable("id")long order_id,Model model,HttpSession session){
        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }else {
                model.addAttribute("payment", "Paid");
        }
        Customer customer=customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        model.addAttribute("order",order);
        model.addAttribute("address",address);

        return "order-view";
    }


}
