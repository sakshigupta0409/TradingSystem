package com.phonepe.models;

import com.phonepe.models.enums.OrderStatus;
import com.phonepe.models.enums.OrderType;

import java.sql.Timestamp;

public class Order {
    private String orderId;
    private String userId;
    private OrderType orderType;
    private String stockSymbol;
    private int quantity;
    private int price;
    private Timestamp orderAcceptedTime;
    private OrderStatus orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getOrderAcceptedTime() {
        return orderAcceptedTime;
    }

    public void setOrderAcceptedTime(Timestamp orderAcceptedTime) {
        this.orderAcceptedTime = orderAcceptedTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
