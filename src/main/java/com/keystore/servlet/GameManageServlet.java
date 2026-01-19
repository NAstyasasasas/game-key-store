package com.keystore.servlet;

import com.keystore.model.User;
import com.keystore.model.Game;
import com.keystore.service.GameService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "GameManageServlet", value = "/admin/games/*")
public class GameManageServlet extends HttpServlet {
    private GameService gameService;

    @Override
    public void init() throws ServletException {
        gameService = new GameService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.SELLER) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String action = request.getPathInfo();

        if (action == null || action.equals("/")) {
            showMyGames(request, response, user);
        } else if (action.equals("/add")) {
            request.getRequestDispatcher("/WEB-INF/jsp/game/add.jsp").forward(request, response);
        } else if (action.startsWith("/edit/")) {
            try {
                int gameId = Integer.parseInt(action.substring(6));
                Game game = gameService.getGameById(gameId);

                if (game != null && game.getSellerId() == user.getId()) {
                    request.setAttribute("game", game);
                    request.getRequestDispatcher("/WEB-INF/jsp/game/edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/games");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/games");
            }
        } else if (action.startsWith("/delete/")) {
            try {
                int gameId = Integer.parseInt(action.substring(8));
                Game game = gameService.getGameById(gameId);

                if (game != null && game.getSellerId() == user.getId()) {
                    if (gameService.updateStock(gameId, 0)) {
                        session.setAttribute("success", "Игра скрыта из каталога");
                    } else {
                        session.setAttribute("error", "Ошибка при скрытии игры");
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/games");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/games");
            }
        }
    }

    private void showMyGames(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Game> myGames = gameService.getGamesBySeller(user.getId());
        request.setAttribute("games", myGames);
        request.getRequestDispatcher("/WEB-INF/jsp/game/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.SELLER) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String action = request.getPathInfo();

        if (action.equals("/add")) {
            addGame(request, response, user, session);
        } else if (action.equals("/edit")) {
            try {
                int gameId = Integer.parseInt(request.getParameter("id"));
                updateGame(request, response, gameId, user, session);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Некорректный ID игры");
                response.sendRedirect(request.getContextPath() + "/admin/games");
            }
        } else if (action.startsWith("/edit/")) {
            try {
                int gameId = Integer.parseInt(action.substring(6));
                updateGame(request, response, gameId, user, session);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/games");
            }
        } else if (action.equals("/delete")) {
            try {
                int gameId = Integer.parseInt(request.getParameter("gameId"));
                Game game = gameService.getGameById(gameId);

                if (game != null && game.getSellerId() == user.getId()) {
                    if (gameService.deleteGame(gameId)) {
                        session.setAttribute("success", "Игра удалена из каталога");
                    } else {
                        session.setAttribute("error", "Ошибка при удалении игры");
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/games");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/games");
            }
        }
    }

    private void addGame(HttpServletRequest request, HttpServletResponse response, User user, HttpSession session) throws IOException {

        try {
            Game game = new Game();
            game.setTitle(request.getParameter("title"));
            game.setDescription(request.getParameter("description"));
            game.setPrice(new BigDecimal(request.getParameter("price")));
            game.setPlatform(Game.Platform.valueOf(request.getParameter("platform").toUpperCase()));
            game.setGenre(request.getParameter("genre"));

            String releaseYear = request.getParameter("releaseYear");
            if (releaseYear != null && !releaseYear.isEmpty()) {
                game.setReleaseYear(Integer.parseInt(releaseYear));
            }

            game.setStock(Integer.parseInt(request.getParameter("stock")));

            boolean success = gameService.addNewGame(game, user);

            if (success) {
                session.setAttribute("success", "Игра успешно добавлена!");
            } else {
                session.setAttribute("error", "Ошибка при добавлении игры");
            }

            response.sendRedirect(request.getContextPath() + "/admin/games");

        } catch (Exception e) {
            session.setAttribute("error", "Некорректные данные: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/games/add");
        }
    }

    private void updateGame(HttpServletRequest request, HttpServletResponse response, int gameId, User user, HttpSession session) throws IOException {

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String platform = request.getParameter("platform");
            String genre = request.getParameter("genre");
            int newStock = Integer.parseInt(request.getParameter("stock"));
            BigDecimal newPrice = new BigDecimal(request.getParameter("price"));

            String releaseYearStr = request.getParameter("releaseYear");
            Integer releaseYear = null;
            if (releaseYearStr != null && !releaseYearStr.trim().isEmpty()) {
                releaseYear = Integer.parseInt(releaseYearStr);
            }

            Game game = gameService.getGameById(gameId);
            if (game != null && game.getSellerId() == user.getId()) {
                game.setTitle(title);
                game.setDescription(description);
                game.setPlatform(Game.Platform.valueOf(platform.toUpperCase()));
                game.setGenre(genre);
                game.setPrice(newPrice);
                game.setStock(newStock);
                if (releaseYear != null) {
                    game.setReleaseYear(releaseYear);
                }

                boolean success = gameService.updateGame(game);

                if (success) {
                    session.setAttribute("success", "Игра обновлена успешно!");
                } else {
                    session.setAttribute("error", "Ошибка обновления игры");
                }
            } else {
                session.setAttribute("error", "Игра не найдена или вы не являетесь её владельцем");
            }

            response.sendRedirect(request.getContextPath() + "/admin/games");

        } catch (Exception e) {
            session.setAttribute("error", "Некорректные данные: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/games");
        }
    }
}