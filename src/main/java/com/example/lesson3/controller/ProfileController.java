package com.example.lesson3.controller;

import com.example.lesson3.model.User;
import com.example.lesson3.service.UserService;
import com.example.lesson3.utils.FileUploadUtil;
import com.example.lesson3.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile/index";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = auth.getName();
            User currentUser = userService.findByEmail(currentEmail);

            // Kiểm tra mật khẩu nếu có thay đổi
            if (password != null && !password.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
                    return "redirect:/profile";
                }
                // Không set mật khẩu ở đây, để UserService xử lý việc hash
            }

            // Kiểm tra email mới có trùng với email khác không
            if (!email.equals(currentEmail)) {
                try {
                    User existingUser = userService.findByEmail(email);
                    if (existingUser != null && !existingUser.getId().equals(currentUser.getId())) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Email này đã được sử dụng!");
                        return "redirect:/profile";
                    }
                } catch (RuntimeException e) {
                    // Email không tồn tại, có thể sử dụng
                }
            }

            // Cập nhật thông tin cơ bản
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            
            // Set mật khẩu mới nếu có
            if (password != null && !password.isEmpty()) {
                String hashedPassword = PasswordUtil.hashPassword(password);
                currentUser.setPassword(hashedPassword);
            }

            // Xử lý ảnh đại diện
            if (imageFile != null && !imageFile.isEmpty()) {
                // Xóa ảnh cũ nếu có
                if (currentUser.getAvatar() != null) {
                    String oldImagePath = currentUser.getAvatar();
                    if (oldImagePath.startsWith("users/")) {
                        oldImagePath = oldImagePath.substring("users/".length());
                    }
                    FileUploadUtil.deleteFile("uploads/users", oldImagePath);
                }
                // Lưu ảnh mới
                String filename = FileUploadUtil.saveFile("uploads/users", imageFile);
                currentUser.setAvatar("users/" + filename);
            }
            // Lưu thông tin user
            userService.saveUser(currentUser);
            
            // Nếu email hoặc mật khẩu thay đổi, yêu cầu đăng nhập lại
            if (!email.equals(currentEmail) || (password != null && !password.isEmpty())) {
                SecurityContextHolder.clearContext();
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công! Vui lòng đăng nhập lại.");
                return "redirect:/login";
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin!");
            e.printStackTrace();
        }

        return "redirect:/profile";
    }
}
