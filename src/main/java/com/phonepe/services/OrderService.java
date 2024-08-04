package com.phonepe.services;

import com.phonepe.models.Order;
import com.phonepe.models.OrderStatus;
import com.phonepe.models.OrderType;
import com.phonepe.models.Trade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderService {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final DbService dbService;
    private final TradeService tradeService;

    public OrderService(DbService dbService, TradeService tradeService) {
        this.dbService = dbService;
        this.tradeService = tradeService;
    }

    public void placeOrder(Order order) {
        lock.writeLock().lock();
        try {
            dbService.addOrder(order);
            dbService.addToOrderBook(order);
            System.out.println("Order placed: " + order);
            executeOrders(order.getStockId());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void modifyOrder(String orderId, int newQuantity, double newPrice) {
        lock.writeLock().lock();
        try {
            Order oldOrder = dbService.getOrder(orderId);
            if (oldOrder != null && oldOrder.getStatus() == OrderStatus.ACCEPTED) {
                Order newOrder = new Order(oldOrder.getOrderId(), oldOrder.getAccountId(), oldOrder.getStockId(),
                        oldOrder.getOrderType(), newQuantity, newPrice, Instant.now());
                dbService.addOrder(newOrder);

                PriorityQueue<Order> orderBook = dbService.getOrderBookForStockId(oldOrder.getStockId());
                orderBook.remove(oldOrder);
                orderBook.add(newOrder);
                System.out.println("Order modified: " + newOrder);
                executeOrders(newOrder.getStockId());
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
            Order order = dbService.getOrder(orderId);
            if (order != null && order.getStatus() == OrderStatus.ACCEPTED) {
                order.setStatus(OrderStatus.CANCELLED);

                PriorityQueue<Order> orderBook = dbService.getOrderBookForStockId(order.getStockId());
                orderBook.remove(order);
                System.out.println("Order canceled: " + order);
            } else {
                System.out.println("Order cannot be canceled or does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void showOrder(String orderId) {
        lock.readLock().lock();
        try {
            Order order = dbService.getOrder(orderId);
            if (order != null) {
                System.out.println("Order status: " + order);
            } else {
                System.out.println("Order not found.");
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    private void executeOrders(String stockId) {

        PriorityQueue<Order> orderBook = dbService.getOrderBookForStockId(stockId);
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
            if (buyOrder.getOrderType() == OrderType.BUY) {
                for (int j = i + 1; j < ordersToMatch.size(); j++) {
                    Order sellOrder = ordersToMatch.get(j);
                    if (sellOrder.getOrderType() == OrderType.SELL && buyOrder.getPrice() >= sellOrder.getPrice()) {

                        int quantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                        Trade trade = new Trade(
                                "T" + System.currentTimeMillis(), // Simple trade ID
                                OrderType.BUY,
                                buyOrder.getOrderId(),
                                sellOrder.getOrderId(),
                                stockId,
                                quantity,
                                sellOrder.getPrice(),
                                Instant.now()
                        );
                        trades.add(trade);
                        tradeService.addTrade(trade);


                        buyOrder = new Order(buyOrder.getOrderId(), buyOrder.getAccountId(), buyOrder.getStockId(), buyOrder.getOrderType(),
                                buyOrder.getQuantity() - quantity, buyOrder.getPrice(), buyOrder.getAcceptedTimestamp());
                        sellOrder = new Order(sellOrder.getOrderId(), sellOrder.getAccountId(), sellOrder.getStockId(), sellOrder.getOrderType(),
                                sellOrder.getQuantity() - quantity, sellOrder.getPrice(), sellOrder.getAcceptedTimestamp());
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
