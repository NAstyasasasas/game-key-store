package com.keystore.dao;

import com.keystore.model.Game;
import com.keystore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT g.*, u.username as seller_name " +
                     "FROM games g " +
                     "JOIN users u ON g.seller_id = u.id " +
                     "WHERE g.is_available = TRUE ORDER BY g.created_at DESC";

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setDescription(rs.getString("description"));
                game.setPrice(rs.getBigDecimal("price"));
                game.setPlatform(Game.Platform.valueOf(rs.getString("platform")));
                game.setGenre(rs.getString("genre"));
                game.setReleaseYear(rs.getInt("release_year"));
                game.setSellerId(rs.getInt("seller_id"));
                game.setSellerName(rs.getString("seller_name"));
                game.setStock(rs.getInt("stock"));
                game.setAvailable(rs.getBoolean("is_available"));
                game.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                games.add(game);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all games: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }

        return games;
    }

    public Game getGameById(int id) {
        Game game = null;
        String sql = "SELECT * FROM games WHERE id = ?";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setDescription(rs.getString("description"));
                game.setPrice(rs.getBigDecimal("price"));
                game.setPlatform(Game.Platform.valueOf(rs.getString("platform")));
                game.setGenre(rs.getString("genre"));
                game.setReleaseYear(rs.getInt("release_year"));
                game.setSellerId(rs.getInt("seller_id"));
                game.setStock(rs.getInt("stock"));
                game.setAvailable(rs.getBoolean("is_available"));
                game.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Error getting game by id: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return game;
    }

    public boolean addGame(Game game) {
        String sql = "INSERT INTO games (title, description, price, platform, genre, release_year, seller_id, stock) " +
                "VALUES (?, ?, ?, ?::platform_type, ?, ?, ?, ?)";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDescription());
            pstmt.setBigDecimal(3, game.getPrice());
            pstmt.setString(4, game.getPlatform().name());
            pstmt.setString(5, game.getGenre());

            if (game.getReleaseYear() != null) {
                pstmt.setInt(6, game.getReleaseYear());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.setInt(7, game.getSellerId());
            pstmt.setInt(8, game.getStock());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding game: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public boolean updateGameStock(int gameId, int newStock) {
        String sql = "UPDATE games SET stock = ? WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, newStock);
            pstmt.setInt(2, gameId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating game stock: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public boolean updateGame(Game game) {
        String sql = "UPDATE games SET price = ?, stock = ? WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setBigDecimal(1, game.getPrice());
            pstmt.setInt(2, game.getStock());
            pstmt.setInt(3, game.getId());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating game: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    public boolean deleteGame(int gameId) {
        String sql = "UPDATE games SET is_available = FALSE WHERE id = ?";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, gameId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting game: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
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
    public List<Game> getGamesBySeller(int sellerId) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games WHERE seller_id = ? AND is_available = TRUE ORDER BY created_at DESC";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sellerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setDescription(rs.getString("description"));
                game.setPrice(rs.getBigDecimal("price"));
                game.setPlatform(Game.Platform.valueOf(rs.getString("platform")));
                game.setGenre(rs.getString("genre"));
                game.setReleaseYear(rs.getInt("release_year"));
                game.setSellerId(rs.getInt("seller_id"));
                game.setStock(rs.getInt("stock"));
                game.setAvailable(rs.getBoolean("is_available"));
                game.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                games.add(game);
            }
        } catch (SQLException e) {
            System.err.println("Error getting seller games: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return games;
    }
}