package com.keystore.service;

import com.keystore.dao.OrderDao;
import com.keystore.dao.GameDao;
import com.keystore.dao.UserDao;
import com.keystore.model.Order;
import com.keystore.model.OrderItem;
import com.keystore.model.Game;
import com.keystore.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private OrderDao orderDao;
    private GameDao gameDao;
    private UserDao userDao;

    public OrderService() {
        this.orderDao = new OrderDao();
        this.gameDao = new GameDao();
        this.userDao = new UserDao();
    }

    public boolean checkout(int userId, Map<Integer, Integer> cart) {
        if (cart == null || cart.isEmpty()) {
            System.err.println("Cart is empty");
            return false;
        }

        User user = userDao.getUserById(userId);
        if (user == null) {
            System.err.println("User not found: " + userId);
            return false;
        }

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int gameId = entry.getKey();
            int quantity = entry.getValue();

            Game game = gameDao.getGameById(gameId);
            if (game == null) {
                System.err.println("Game not found: " + gameId);
                return false;
            }

            if (game.getStock() < quantity) {
                System.err.println("Not enough stock for game: " + game.getTitle());
                return false;
            }

            OrderItem item = new OrderItem();
            item.setGameId(gameId);
            item.setQuantity(quantity);
            item.setPriceAtTime(game.getPrice());

            items.add(item);

            BigDecimal itemTotal = game.getPrice().multiply(new BigDecimal(quantity));
            total = total.add(itemTotal);
        }

        if (user.getBalance().compareTo(total) < 0) {
            System.err.println("Insufficient balance");
            return false;
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus(Order.Status.PAID);
        order.setItems(items);

        return orderDao.createOrder(order);
    }

    public List<Order> getUserOrders(int userId) {
        return orderDao.getUserOrders(userId);
    }
}