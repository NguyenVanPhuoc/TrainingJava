package com.example.lesson3.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.lesson3.model.CustomUserDetails;

import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalUserInfoAdvice {

    @ModelAttribute
    public void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                CustomUserDetails user = (CustomUserDetails) principal;
                String avatar = user.getAvatar();
                if (avatar == null || avatar.isEmpty()) {
                    avatar = "https://picsum.photos/40";
                } else {
                    avatar = "/uploads/" + avatar;
                }
                System.out.println(avatar);
                model.addAttribute("avatarUrl", avatar);
                model.addAttribute("username", user.getUsername());
                // bạn có thể add thêm các thông tin khác như role, email, ...
            }
        }
    }
}

