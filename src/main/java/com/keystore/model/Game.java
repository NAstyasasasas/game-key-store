package com.keystore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game {
    private int id;
    private String title;
    private String description;
    private BigDecimal price;
    private Platform platform;
    private String genre;
    private Integer releaseYear;
    private int sellerId;
    private String sellerName;
    private int stock;
    private boolean available;
    private LocalDateTime createdAt;

    public enum Platform {
        PC, PS5, XBOX, SWITCH, MOBILE, MULTI
    }

    public Game() {
    }

    public Game(int id, String title, String description, BigDecimal price,
                Platform platform, String genre, Integer releaseYear,
                int sellerId, int stock, boolean available, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.platform = platform;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.sellerId = sellerId;
        this.stock = stock;
        this.available = available;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public Platform getPlatform() {
        return platform;
    }
    public String getGenre() {
        return genre;
    }
    public Integer getReleaseYear() {
        return releaseYear;
    }
    public int getSellerId() {
        return sellerId;
    }
    public int getStock() {
        return stock;
    }
    public String getSellerName() {
        return sellerName;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", platform=" + platform +
                ", stock=" + stock +
                '}';
    }

}