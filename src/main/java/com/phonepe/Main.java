package com.phonepe;

import com.phonepe.admin.AdminService;
import com.phonepe.models.*;
import com.phonepe.services.*;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        DbService dbService = new DbService();

        AccountService accountService = new AccountService(dbService);
        TradeService tradeService = new TradeService(dbService);
        OrderService orderService = new OrderService(dbService, tradeService);
        AppService appService = new AppService(accountService, orderService, tradeService);

        UserService userService = new UserService(dbService);
        StockService stockService = new StockService(dbService);
        AdminService adminService = new AdminService(userService, stockService, accountService);

        // Loading Dummy Data in System
        User user1 = new User("U1", "Alice", "1234567890", "alice@example.com");
        User user2 = new User("U2", "Bob", "0987654321", "bob@example.com");
        adminService.addUser(user1);
        adminService.addUser(user2);
        Account account1 = new Account("ACC01", user1.getUserId(), "DMT01", 100000d);
        Account account2 = new Account("ACC02", user1.getUserId(), "DMT02", 500000d);
        adminService.addAccount(account1);
        adminService.addAccount(account2);
        Stock stock1 = new Stock("S01", "IDEA", "VODAFONE IDEA", "Telecom Stock");
        Stock stock2 = new Stock("S02", "VEDL", "VEDANTA LIMITED", "Metal Stock");
        adminService.addStock(stock1);
        adminService.addStock(stock2);

        // Main Code

        Runnable task1 = () -> {
            Order order1 = new Order("O1", account1.getAccountId(), stock1.getStockId(), OrderType.BUY,
                    100, 150.0, Instant.now());
            appService.placeOrder(order1);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.modifyOrder("O1", 150, 155.0);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.cancelOrder("O1");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.showOrder("O1");
        };

        Runnable task2 = () -> {
            Order order2 = new Order("O2", account1.getAccountId(), stock1.getStockId(), OrderType.SELL,
                    200, 150.0, Instant.now());
            appService.placeOrder(order2);

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.showOrder("O2");

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.cancelOrder("O2");

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.showOrder("O2");
        };

        Runnable tradeTask = () -> {
            Order buyOrder = new Order("O3", account1.getAccountId(), stock1.getStockId(), OrderType.BUY,
                    100, 150.0, Instant.now());
            Order sellOrder = new Order("O4", account1.getAccountId(), stock1.getStockId(), OrderType.SELL,
                    100, 150.0, Instant.now());
            appService.placeOrder(buyOrder);
            appService.placeOrder(sellOrder);

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            appService.showOrder("O3");
            appService.showOrder("O4");
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread tradeThread = new Thread(tradeTask);

        thread1.start();
        thread2.start();
        tradeThread.start();

        try {
            thread1.join();
            thread2.join();
            tradeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
