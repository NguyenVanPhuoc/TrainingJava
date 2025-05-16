package com.example.lesson3.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private static BCryptPasswordEncoder encoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        PasswordUtil.encoder = passwordEncoder;
    }

    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
