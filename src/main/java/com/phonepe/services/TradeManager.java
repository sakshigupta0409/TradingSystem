package com.phonepe.services;

import com.phonepe.models.Trade;

import java.util.HashMap;
import java.util.Map;

public class TradeManager {
    private final Map<String, Trade> trades = new HashMap<>();

    public void addTrade(Trade trade) {
        trades.put(trade.getTradeId(), trade);
        System.out.println("Trade added: " + trade);
    }

    public void queryTrade(String tradeId) {
        Trade trade = trades.get(tradeId);
        if (trade != null) {
            System.out.println("Trade details: " + trade);
        } else {
            System.out.println("Trade not found.");
        }
    }
}


