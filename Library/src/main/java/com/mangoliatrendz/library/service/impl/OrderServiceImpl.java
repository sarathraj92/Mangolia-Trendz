package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.model.*;
import com.mangoliatrendz.library.repository.OrderDetailRepository;
import com.mangoliatrendz.library.repository.OrderRepository;
import com.mangoliatrendz.library.repository.ProductRepository;
import com.mangoliatrendz.library.service.AddressService;
import com.mangoliatrendz.library.service.OrderService;
import com.mangoliatrendz.library.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    private ShoppingCartService shoppingCartService;

    private ProductRepository productRepository;

    private AddressService addressService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ShoppingCartService shoppingCartService,
                            ProductRepository productRepository,AddressService addressService) {
        this.addressService=addressService;
        this.productRepository=productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Order save(ShoppingCart cart,long address_id,String paymentMethod) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrice());
        order.setQuantity(cart.getTotalItems());
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(addressService.findByIdOrder(address_id));
        order.setAccept(false);
        order.setOrderStatus("Pending");
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for(CartItem item : cart.getCartItems()){
            Product product=item.getProduct();
            int currentQuantity = product.getCurrentQuantity();
            product.setCurrentQuantity(currentQuantity - item.getQuantity());
            productRepository.save(product);
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setSize(item.getSize());
            orderDetail.setQuantity(item.getQuantity());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetails(orderDetailList);
        shoppingCartService.deleteCartById(cart.getId());

        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(long order_id) {
        Order order=orderRepository.getById(order_id);
        List<OrderDetail>orderDetailList=order.getOrderDetails();
        for(OrderDetail orderDetail : orderDetailList){
            Product product = orderDetail.getProduct();
            int currentQuantity= product.getCurrentQuantity();
            product.setCurrentQuantity(currentQuantity + orderDetail.getQuantity());
            productRepository.save(product);
        }
        order.setOrderStatus("Cancelled");
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void acceptOrder(long id) {
         Order order=orderRepository.getById(id);
         order.setAccept(true);
         Date oldDate = new Date();
         LocalDate localDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         LocalDate newLocalDate = localDate.plusDays(5);
         Date newDate = Date.from(newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
         order.setDeliveryDate(newDate);
         order.setOrderStatus("Confirmed");
         orderRepository.save(order);
    }

    @Override
    public Order findOrderById(long id) {
        return orderRepository.getById(id);
    }

    @Override
    public List<Order> findAllOrdersByCustomer(long id) {
        return orderRepository.findAllBy(id);
    }

    @Override
    public List<Long> findAllOrderCountForEachMonth() {
        List<Long>orderCounts=new ArrayList<>();
        LocalDate currentDate = LocalDate.now().withMonth(1);

        for(int i=0 ; i < 12 ; i++){
            LocalDate localStartDate=currentDate.withDayOfMonth(1);
            LocalDate localEndDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            long orderCount= orderRepository.countByOrderDateBetweenAndOrderStatus(localStartDate,localEndDate,"Confirmed");
            orderCounts.add(orderCount);
            currentDate = currentDate.plusMonths(1);

        }


        return orderCounts;
    }

    @Override
    public Double getTotalOrderAmount() {

        return orderRepository.getTotalConfirmedOrdersAmount();
    }

    @Override
    public Long countTotalConfirmedOrders() {
        return orderRepository.countAllConfirmedOrders();
    }

    @Override
    public Double getTotalAmountForMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate=currentDate.withDayOfMonth(1);
        LocalDate endDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        Double totalAmount = orderRepository.getTotalConfirmedOrdersAmountForMonth(startDate,endDate,"Confirmed");

        return totalAmount;
    }

    @Override
    public List<Double> getTotalAmountForEachMonth() {

        List<Double> totalRevenuePerMonth = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().withMonth(1);

        for(int i=0 ; i < 12 ; i++){
            LocalDate localStartDate=currentDate.withDayOfMonth(1);
            LocalDate localEndDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            Double totalRevenue = orderRepository.getTotalConfirmedOrdersAmountForMonth(localStartDate,localEndDate,"Confirmed");
            totalRevenuePerMonth.add(totalRevenue);
            currentDate = currentDate.plusMonths(1);

        }

        return totalRevenuePerMonth;
    }
}