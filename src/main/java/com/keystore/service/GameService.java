package com.keystore.service;

import com.keystore.dao.GameDao;
import com.keystore.dao.UserDao;
import com.keystore.model.Game;
import com.keystore.model.User;

import java.util.List;

public class GameService {
    private GameDao gameDao;
    private UserDao userDao;

    public GameService() {
        this.gameDao = new GameDao();
        this.userDao = new UserDao();
    }

    public List<Game> getGamesBySeller(int sellerId) {
        return gameDao.getGamesBySeller(sellerId);
    }

    public List<Game> getAllAvailableGames() {
        return gameDao.getAllGames();
    }

    public Game getGameById(int id) {
        return gameDao.getGameById(id);
    }

    public boolean addNewGame(Game game, User seller) {
        if (seller.getRole() != User.Role.SELLER) {
            System.err.println("User is not a seller");
            return false;
        }

        if (game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            System.err.println("Game title is required");
            return false;
        }

        if (game.getPrice() == null || game.getPrice().doubleValue() <= 0) {
            System.err.println("Invalid price");
            return false;
        }

        if (game.getStock() < 0) {
            System.err.println("Stock cannot be negative");
            return false;
        }

        game.setSellerId(seller.getId());
        game.setAvailable(true);

        return gameDao.addGame(game);
    }

    public boolean checkStock(int gameId, int quantity) {
        Game game = gameDao.getGameById(gameId);
        if (game == null) {
            return false;
        }
        return game.getStock() >= quantity;
    }

    public boolean updateStock(int gameId, int newStock) {
        if (newStock < 0) {
            return false;
        }
        return gameDao.updateGameStock(gameId, newStock);
    }

    public boolean updateGame(Game game) {
        if (game == null) {
            return false;
        }
        if (game.getPrice() == null || game.getPrice().doubleValue() <= 0) {
            System.err.println("Invalid price");
            return false;
        }
        if (game.getStock() < 0) {
            System.err.println("Stock cannot be negative");
            return false;
        }
        return gameDao.updateGame(game);
    }

    public boolean deleteGame(int gameId) {
        return gameDao.deleteGame(gameId);
    }
}