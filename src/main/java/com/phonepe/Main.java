package com.phonepe;

import com.phonepe.models.Order;
import com.phonepe.models.User;
import com.phonepe.services.OrderManager;
import com.phonepe.services.TradeManager;

import java.time.Instant;
public class Main {
    public static void main(String[] args) {
        TradeManager tradeManager = new TradeManager();
        OrderManager orderManager = new OrderManager(tradeManager);

        User user1 = new User("U1", "Alice", "1234567890", "alice@example.com");
        User user2 = new User("U2", "Bob", "0987654321", "bob@example.com");

        Runnable task1 = () -> {
            Order order1 = new Order("O1", user1.getUserId(), Order.OrderType.BUY, "AAPL", 100, 150.0, Instant.now());
            orderManager.placeOrder(order1);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.modifyOrder("O1", 150, 155.0);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.cancelOrder("O1");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.queryOrder("O1");
        };

        Runnable task2 = () -> {
            Order order2 = new Order("O2", user2.getUserId(), Order.OrderType.SELL, "AAPL", 200, 150.0, Instant.now());
            orderManager.placeOrder(order2);

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.queryOrder("O2");

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.cancelOrder("O2");

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.queryOrder("O2");
        };

        Runnable tradeTask = () -> {
            Order buyOrder = new Order("O3", user1.getUserId(), Order.OrderType.BUY, "AAPL", 100, 150.0, Instant.now());
            Order sellOrder = new Order("O4", user2.getUserId(), Order.OrderType.SELL, "AAPL", 100, 150.0, Instant.now());
            orderManager.placeOrder(buyOrder);
            orderManager.placeOrder(sellOrder);

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            orderManager.queryOrder("O3");
            orderManager.queryOrder("O4");
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


