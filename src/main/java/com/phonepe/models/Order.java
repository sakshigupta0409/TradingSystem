package com.phonepe.models;

import java.time.Instant;

public class Order {
    private final String orderId;
    private final String userId;
    private final OrderType orderType;
    private final String stockSymbol;
    private final int quantity;
    private final double price;
    private final Instant acceptedTimestamp;
    private Status status;

    public Order(String orderId, String userId, OrderType orderType, String stockSymbol, int quantity, double price, Instant acceptedTimestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.acceptedTimestamp = acceptedTimestamp;
        this.status = Status.ACCEPTED;
    }

    public double getPrice() {
        return price;
    }

    public Instant getAcceptedTimestamp() {
        return acceptedTimestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderType=" + orderType +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", acceptedTimestamp=" + acceptedTimestamp +
                ", status=" + status +
                '}';
    }

    public enum OrderType {
        BUY, SELL
    }

    public enum Status {
        ACCEPTED, REJECTED, CANCELED, EXECUTED
    }
}

