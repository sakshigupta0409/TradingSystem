package com.phonepe.services;

import com.phonepe.models.Order;

public class AppService {
    private final AccountService accountService;
    private final OrderService orderService;
    private final TradeService tradeService;

    public AppService(AccountService accountService, OrderService orderService, TradeService tradeService) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.tradeService = tradeService;
    }

    public void placeOrder(Order order) {
        orderService.placeOrder(order);
    }

    public void modifyOrder(String orderId, int newQuantity, double newPrice) {
        orderService.modifyOrder(orderId, newQuantity, newPrice);
    }

    public void cancelOrder(String orderId) {
        orderService.cancelOrder(orderId);
    }

    public void showOrder(String orderId) {
        orderService.showOrder(orderId);
    }
}
