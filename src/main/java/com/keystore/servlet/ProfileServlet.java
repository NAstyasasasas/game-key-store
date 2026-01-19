package com.keystore.servlet;

import com.keystore.dao.UserDao;
import com.keystore.model.User;
import com.keystore.service.OrderService;
import com.keystore.model.Order;

import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            List<Order> orders = orderService.getUserOrders(user.getId());
            request.setAttribute("orders", orders);

            request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("ERROR in ProfileServlet.doGet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error loading profile", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if ("topup".equals(action)) {
            try {
                BigDecimal amount = new BigDecimal(request.getParameter("amount"));
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal newBalance = user.getBalance().add(amount);

                    UserDao userDao = new UserDao();
                    if (userDao.updateUserBalance(user.getId(), newBalance)) {
                        user.setBalance(newBalance);
                        session.setAttribute("user", user);
                        session.setAttribute("success", "Баланс успешно пополнен на " + amount + " ₽");
                    } else {
                        session.setAttribute("error", "Ошибка пополнения баланса");
                    }
                } else {
                    session.setAttribute("error", "Сумма должна быть больше 0");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Некорректная сумма");
            }
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}