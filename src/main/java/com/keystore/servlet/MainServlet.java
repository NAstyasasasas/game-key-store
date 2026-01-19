package com.keystore.servlet;

import com.keystore.service.GameService;
import com.keystore.model.Game;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private GameService gameService;

    @Override
    public void init() throws ServletException {
        gameService = new GameService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Game> games = gameService.getAllAvailableGames();
        request.setAttribute("games", games);

        request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
    }
}