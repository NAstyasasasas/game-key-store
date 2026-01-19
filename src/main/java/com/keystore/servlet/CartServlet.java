package com.keystore.servlet;

import com.keystore.model.User;
import com.keystore.service.GameService;
import com.keystore.service.OrderService;
import com.keystore.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartServlet", value = "/cart/*")
public class CartServlet extends HttpServlet {
    private GameService gameService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        gameService = new GameService();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/order/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String action = request.getPathInfo();

        if (action.equals("/add")) {
            addToCart(request, response, session);
        } else if (action.equals("/remove")) {
            removeFromCart(request, response, session);
        } else if (action.equals("/clear")) {
            clearCart(request, response, session);
        } else if (action.equals("/checkout")) {
            checkout(request, response, session);
        }
    }

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest request, HttpServletResponse response,
                           HttpSession session) throws IOException {

        try {
            int gameId = Integer.parseInt(request.getParameter("gameId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
                session.setAttribute("cart", cart);
            }

            int currentQuantity = cart.getOrDefault(gameId, 0);
            int totalQuantity = currentQuantity + quantity;

            if (!gameService.checkStock(gameId, totalQuantity)) {
                session.setAttribute("error", "Недостаточно товара в наличии");
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }

            cart.put(gameId, totalQuantity);

            session.setAttribute("success", "Товар добавлен в корзину");
            response.sendRedirect(request.getContextPath() + "/");

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Ошибка в данных");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @SuppressWarnings("unchecked")
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response,
                                HttpSession session) throws IOException {

        try {
            int gameId = Integer.parseInt(request.getParameter("gameId"));

            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            if (cart != null) {
                cart.remove(gameId);
            }

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Ошибка в данных");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response,
                           HttpSession session) throws IOException {

        session.removeAttribute("cart");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @SuppressWarnings("unchecked")
    private void checkout(HttpServletRequest request, HttpServletResponse response,
                         HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("error", "Корзина пуста");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        boolean success = orderService.checkout(user.getId(), cart);

        if (success) {
            UserDao userDao = new UserDao();
            User updatedUser = userDao.getUserByUsername(user.getUsername());
            if (updatedUser != null) {
                session.setAttribute("user", updatedUser);
            }

            session.removeAttribute("cart");

            session.setAttribute("success", "Заказ успешно оформлен!");
            response.sendRedirect(request.getContextPath() + "/profile");
        } else {
            session.setAttribute("error", "Ошибка при оформлении заказа");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}