package com.keystore.service;

import com.keystore.dao.UserDao;
import com.keystore.model.User;
import com.keystore.util.PasswordHasher;
import com.keystore.util.Validator;

import java.math.BigDecimal;

public class AuthService {
    private UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String username, String password) {
        if (!Validator.validateUsername(username) || !Validator.validatePassword(password)) {
            return null;
        }

        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return null;
        }

        if (!PasswordHasher.checkPassword(password, user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    public boolean register(String username, String email, String password, User.Role role) {
        if (!Validator.validateUsername(username)) {
            System.err.println("Invalid username");
            return false;
        }

        if (!Validator.validateEmail(email)) {
            System.err.println("Invalid email");
            return false;
        }

        if (!Validator.validatePassword(password)) {
            System.err.println("Invalid password");
            return false;
        }

        if (userDao.usernameExists(username)) {
            System.err.println("Username already exists");
            return false;
        }

        if (userDao.emailExists(email)) {
            System.err.println("Email already exists");
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(PasswordHasher.hashPassword(password));
        user.setRole(role);
        user.setBalance(new BigDecimal("0.00"));
        user.setActive(true);

        return userDao.addUser(user);
    }
}