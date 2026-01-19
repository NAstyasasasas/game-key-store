package com.keystore.dao;

import com.keystore.model.User;
import com.keystore.util.DBConnection;
import com.keystore.util.PasswordHasher;

import java.sql.*;
import java.math.BigDecimal;

public class UserDao {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;


    public User getUserByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                user.setBalance(rs.getBigDecimal("balance"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setActive(rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return user;
    }

    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ? AND is_active = TRUE";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                user.setBalance(rs.getBigDecimal("balance"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setActive(rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by id: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return user;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, role, balance) " +
                "VALUES (?, ?, ?, ?::user_role, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getRole().name());
            pstmt.setBigDecimal(5, user.getBalance());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public boolean updateUserBalance(int userId, BigDecimal newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setBigDecimal(1, newBalance);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user balance: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return false;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return false;
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