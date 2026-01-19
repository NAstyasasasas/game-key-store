package com.keystore.dao;

import com.keystore.model.Order;
import com.keystore.model.OrderItem;
import com.keystore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    Connection conn = null;
    PreparedStatement orderStmt = null;
    PreparedStatement itemStmt = null;
    ResultSet generatedKeys = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public boolean createOrder(Order order) {

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?::order_status)";
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getUserId());
            orderStmt.setBigDecimal(2, order.getTotalAmount());
            orderStmt.setString(3, order.getStatus().name());

            int rows = orderStmt.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return false;
            }

            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            String itemSql = "INSERT INTO order_items (order_id, game_id, quantity, price_at_time, game_key) VALUES (?, ?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);

            for (OrderItem item : order.getItems()) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getGameId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setBigDecimal(4, item.getPriceAtTime());
                itemStmt.setString(5, generateGameKey()); // Генерируем ключ
                itemStmt.addBatch();
            }

            int[] itemRows = itemStmt.executeBatch();

            String updateStockSql = "UPDATE games SET stock = stock - ? WHERE id = ?";
            PreparedStatement stockStmt = conn.prepareStatement(updateStockSql);

            for (OrderItem item : order.getItems()) {
                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setInt(2, item.getGameId());
                stockStmt.addBatch();
            }

            stockStmt.executeBatch();
            stockStmt.close();

            String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(updateBalanceSql);
            balanceStmt.setBigDecimal(1, order.getTotalAmount());
            balanceStmt.setInt(2, order.getUserId());
            balanceStmt.executeUpdate();
            balanceStmt.close();

            String updateSellerBalanceSql = "UPDATE users SET balance = balance + ? " +
                    "WHERE id = (SELECT seller_id FROM games WHERE id = ?)";
            PreparedStatement sellerBalanceStmt = conn.prepareStatement(updateSellerBalanceSql);

            for (OrderItem item : order.getItems()) {
                java.math.BigDecimal itemTotal = item.getPriceAtTime().multiply(
                        new java.math.BigDecimal(item.getQuantity()));
                sellerBalanceStmt.setBigDecimal(1, itemTotal);
                sellerBalanceStmt.setInt(2, item.getGameId());
                sellerBalanceStmt.addBatch();
            }

            sellerBalanceStmt.executeBatch();
            sellerBalanceStmt.close();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back: " + ex.getMessage());
            }
            System.err.println("Error creating order: " + e.getMessage());
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (itemStmt != null) itemStmt.close();
                if (orderStmt != null) orderStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(Order.Status.valueOf(rs.getString("status")));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                order.setItems(getOrderItems(order.getId()));

                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user orders: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return orders;
    }

    private List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, g.title FROM order_items oi " +
                "JOIN games g ON oi.game_id = g.id " +
                "WHERE oi.order_id = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setGameId(rs.getInt("game_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPriceAtTime(rs.getBigDecimal("price_at_time"));
                item.setGameKey(rs.getString("game_key"));

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return items;
    }

    private String generateGameKey() {
        return "KEY-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}