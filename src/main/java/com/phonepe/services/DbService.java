package com.phonepe.services;

import com.phonepe.models.*;
import com.phonepe.tables.*;

import java.util.PriorityQueue;

public class DbService {
    private final AccountsTable accountsTable = new AccountsTable();
    private final OrderBookTable orderBookTable = new OrderBookTable();
    private final OrdersTable ordersTable = new OrdersTable();
    private final StocksTable stocksTable = new StocksTable();
    private final TradeTable tradeTable = new TradeTable();
    private final UsersTable usersTable = new UsersTable();

    // Users table queries
    public boolean addUser(User user) {
        return usersTable.addUser(user);
    }

    // Account table queries
    public boolean addAccount(Account account) {
        return accountsTable.addAccount(account);
    }

    // Stocks table queries
    public boolean addStock(Stock stock) {
        return stocksTable.addStock(stock);
    }

    // Order table queries
    public Order addOrder(Order order) {
        return ordersTable.addOrder(order);
    }

    public Order getOrder(String orderId) {
        return ordersTable.getOrderById(orderId);
    }

    public Order removeOrder(String orderId) {
        return ordersTable.removeOrder(orderId);
    }


    // OrderBook table queries
    public boolean addToOrderBook(Order order) {
        return orderBookTable.addOrder(order);
    }

    public PriorityQueue<Order> getOrderBookForStockId(String stockId) {
        return orderBookTable.getOrderBookForStockId(stockId);
    }

    // Trade table queries
    public Trade addTrade(Trade trade) {
        return tradeTable.addTrade(trade);
    }

    public Trade getTrade(String tradeId) {
        return tradeTable.getTrade(tradeId);
    }

}
