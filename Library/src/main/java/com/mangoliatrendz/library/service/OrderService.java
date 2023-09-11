package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.model.Customer;
import com.mangoliatrendz.library.model.Order;
import com.mangoliatrendz.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {

    Order save(ShoppingCart cart,long address_id,String paymentMethod,Double oldTotalPrice);

    void cancelOrder(long order_id);

    List<Order> findAllOrders();

    void acceptOrder(long id);

    Order findOrderById(long id);

    List<Order> findAllOrdersByCustomer(long id);

    List<Long> findAllOrderCountForEachMonth();

    Double getTotalOrderAmount();

    Long countTotalConfirmedOrders();

    Double getTotalAmountForMonth();

    List<Double> getTotalAmountForEachMonth();

    void updatePayment(Order order,boolean status);

    void updateOrderStatus(String status,long order_id);

    void returnOrder(long id, Customer customer);






}
