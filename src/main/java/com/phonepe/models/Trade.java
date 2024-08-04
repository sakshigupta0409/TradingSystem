package com.phonepe.models;

import java.time.Instant;

public class Trade {
    private final String tradeId;
    private final TradeType tradeType;
    private final String buyerOrderId;
    private final String sellerOrderId;
    private final String stockSymbol;
    private final int quantity;
    private final double price;
    private final Instant tradeTimestamp;

    public Trade(String tradeId, TradeType tradeType, String buyerOrderId, String sellerOrderId, String stockSymbol, int quantity, double price, Instant tradeTimestamp) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.buyerOrderId = buyerOrderId;
        this.sellerOrderId = sellerOrderId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.tradeTimestamp = tradeTimestamp;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public String getTradeId() {
        return tradeId;
    }

    public double getPrice() {
        return price;
    }

    public String getBuyerOrderId() {
        return buyerOrderId;
    }

    public String getSellerOrderId() {
        return sellerOrderId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getTradeTimestamp() {
        return tradeTimestamp;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", tradeType=" + tradeType +
                ", buyerOrderId='" + buyerOrderId + '\'' +
                ", sellerOrderId='" + sellerOrderId + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", tradeTimestamp=" + tradeTimestamp +
                '}';
    }

    public enum TradeType {
        BUY, SELL
    }
}
