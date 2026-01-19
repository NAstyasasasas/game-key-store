package com.keystore.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = ((HttpServletRequest) request).getRequestURI();

        HttpServletRequest req = (HttpServletRequest) request;

        if (requestURI.startsWith(req.getContextPath() + "/css") ||
                requestURI.startsWith(req.getContextPath() + "/js") ||
                requestURI.startsWith(req.getContextPath() + "/images") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".jpg") ||
                requestURI.endsWith(".gif")) {
            chain.doFilter(request, response);
            return;
        }


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            String requestedPage = httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                requestedPage += "?" + httpRequest.getQueryString();
            }

            session = httpRequest.getSession(true);
            session.setAttribute("redirectAfterLogin", requestedPage);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
            return;
        }

        chain.doFilter(request, response);
    }
}