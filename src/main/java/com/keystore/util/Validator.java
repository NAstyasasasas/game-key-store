package com.keystore.util;

import java.util.regex.Pattern;

public class Validator {

    public static boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String pattern = "^[a-zA-Z0-9_]{3,20}$";
        return Pattern.matches(pattern, username);
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(pattern, email);
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
}