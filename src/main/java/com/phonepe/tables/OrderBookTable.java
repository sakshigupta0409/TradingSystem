package com.phonepe.tables;

import com.phonepe.models.Order;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class OrderBookTable {
    private final Map<String, PriorityQueue<Order>> orderBooks = new HashMap<>();

    public boolean addOrder(Order order) {
        return orderBooks
                .computeIfAbsent(order.getStockId(), k -> new PriorityQueue<>(Comparator.comparing(Order::getAcceptedTimestamp)))
                .add(order);
    }

    public PriorityQueue<Order> getOrderBookForStockId(String stockId) {
        return orderBooks.get(stockId);
    }
}
