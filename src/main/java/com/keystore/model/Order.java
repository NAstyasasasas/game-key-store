package com.keystore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private BigDecimal totalAmount;
    private Status status;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    public enum Status {
        PENDING, PAID, CANCELLED, COMPLETED
    }

    public Order() {}


    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public Status getStatus() {
        return status;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

        public void setId(int id) {
        this.id = id;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}