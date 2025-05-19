package com.example.lesson3.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        String hashedPassword = encoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);
        return hashedPassword;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        return encoder.matches(password, hashedPassword);
    }
}
