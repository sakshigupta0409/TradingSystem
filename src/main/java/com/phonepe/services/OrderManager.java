package com.phonepe.services;

import com.phonepe.models.Order;
import com.phonepe.models.Trade;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderManager {
    private final Map<String, Order> orders = new HashMap<>();
    private final Map<String, PriorityQueue<Order>> orderBooks = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final TradeManager tradeManager;

    public OrderManager(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    public void placeOrder(Order order) {
        lock.writeLock().lock();
        try {
            orders.put(order.getOrderId(), order);
            orderBooks
                    .computeIfAbsent(order.getStockSymbol(), k -> new PriorityQueue<>(Comparator.comparing(Order::getAcceptedTimestamp)))
                    .add(order);
            System.out.println("Order placed: " + order);
            executeOrders(order.getStockSymbol());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void modifyOrder(String orderId, int newQuantity, double newPrice) {
        lock.writeLock().lock();
        try {
            Order oldOrder = orders.get(orderId);
            if (oldOrder != null && oldOrder.getStatus() == Order.Status.ACCEPTED) {
                Order newOrder = new Order(orderId, oldOrder.getUserId(), oldOrder.getOrderType(), oldOrder.getStockSymbol(), newQuantity, newPrice, oldOrder.getAcceptedTimestamp());
                orders.put(orderId, newOrder);
                orderBooks.get(oldOrder.getStockSymbol()).remove(oldOrder);
                orderBooks.get(oldOrder.getStockSymbol()).add(newOrder);
                System.out.println("Order modified: " + newOrder);
                executeOrders(oldOrder.getStockSymbol());
            } else {
                System.out.println("Order cannot be modified or does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void cancelOrder(String orderId) {
        lock.writeLock().lock();
        try {
            Order order = orders.get(orderId);
            if (order != null && order.getStatus() == Order.Status.ACCEPTED) {
                order.setStatus(Order.Status.CANCELED);
                orderBooks.get(order.getStockSymbol()).remove(order);
                System.out.println("Order canceled: " + order);
            } else {
                System.out.println("Order cannot be canceled or does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void queryOrder(String orderId) {
        lock.readLock().lock();
        try {
            Order order = orders.get(orderId);
            if (order != null) {
                System.out.println("Order status: " + order);
            } else {
                System.out.println("Order not found.");
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    private void executeOrders(String stockSymbol) {
        PriorityQueue<Order> orderBook = orderBooks.get(stockSymbol);
        if (orderBook == null || orderBook.isEmpty()) {
            return;
        }

        List<Order> ordersToMatch = new ArrayList<>();
        while (!orderBook.isEmpty()) {
            ordersToMatch.add(orderBook.poll());
        }

        // Matching logic
        List<Trade> trades = new ArrayList<>();
        for (int i = 0; i < ordersToMatch.size(); i++) {
            Order buyOrder = ordersToMatch.get(i);
            if (buyOrder.getOrderType() == Order.OrderType.BUY) {
                for (int j = i + 1; j < ordersToMatch.size(); j++) {
                    Order sellOrder = ordersToMatch.get(j);
                    if (sellOrder.getOrderType() == Order.OrderType.SELL && buyOrder.getPrice() >= sellOrder.getPrice()) {
                        int quantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                        Trade trade = new Trade(
                                "T" + System.currentTimeMillis(), // Simple trade ID
                                Trade.TradeType.BUY,
                                buyOrder.getOrderId(),
                                sellOrder.getOrderId(),
                                stockSymbol,
                                quantity,
                                sellOrder.getPrice(),
                                Instant.now()
                        );
                        trades.add(trade);
                        tradeManager.addTrade(trade);
                        buyOrder = new Order(buyOrder.getOrderId(), buyOrder.getUserId(), buyOrder.getOrderType(), buyOrder.getStockSymbol(), buyOrder.getQuantity() - quantity, buyOrder.getPrice(), buyOrder.getAcceptedTimestamp());
                        sellOrder = new Order(sellOrder.getOrderId(), sellOrder.getUserId(), sellOrder.getOrderType(), sellOrder.getStockSymbol(), sellOrder.getQuantity() - quantity, sellOrder.getPrice(), sellOrder.getAcceptedTimestamp());
                        if (buyOrder.getQuantity() > 0) {
                            orderBook.add(buyOrder);
                        }
                        if (sellOrder.getQuantity() > 0) {
                            orderBook.add(sellOrder);
                        }
                        break;
                    }
                }
            }
        }

        // Log trades
        for (Trade trade : trades) {
            System.out.println("Trade executed: " + trade);
        }
    }
}

