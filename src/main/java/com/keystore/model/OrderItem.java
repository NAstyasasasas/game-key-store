package com.keystore.model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private int orderId;
    private int gameId;
    private int quantity;
    private BigDecimal priceAtTime;
    private String gameKey;
    private Game game;

    public OrderItem() {}


    public int getId() {
        return id;
    }
    public int getOrderId() {
        return orderId;
    }
    public int getGameId() {
        return gameId;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getPriceAtTime() {
        return priceAtTime;
    }
    public Game getGame() {
        return game;
    }

    public BigDecimal getPrice() {
        return priceAtTime;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPriceAtTime(BigDecimal priceAtTime) {
        this.priceAtTime = priceAtTime;
    }
    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", gameId=" + gameId +
                ", quantity=" + quantity +
                ", priceAtTime=" + priceAtTime +
                '}';
    }
}
