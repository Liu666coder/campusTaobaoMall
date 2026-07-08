package com.campus.campusTaobaoMall.controller;

import com.campus.campusTaobaoMall.dto.LoginRequest;
import com.campus.campusTaobaoMall.entity.Admin;
import com.campus.campusTaobaoMall.service.AdminService;
import com.campus.campusTaobaoMall.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 管理员管理控制器
 * 提供管理员登录、信息获取、管理员增删改查、密码重置、个人资料修改及头像上传等接口
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Value("${upload.path:}")
    private String uploadPath;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        return adminService.login(request);
    }

    /**
     * 获取当前登录管理员信息
     */
    @GetMapping("/info")
    public Result<?> getAdminInfo(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        return adminService.getAdminInfo(adminId);
    }

    /**
     * 获取管理员列表（支持关键字和角色筛选）
     */
    @GetMapping("/list")
    public Result<?> getAdminList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {
        return adminService.getAdminList(keyword, role);
    }

    /**
     * 新增管理员
     */
    @PostMapping
    public Result<?> addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/{id}")
    public Result<?> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        admin.setId(id);
        return adminService.updateAdmin(admin);
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }

    /**
     * 启用/禁用管理员
     */
    @PutMapping("/{id}/status")
    public Result<?> updateAdminStatus(@PathVariable Long id, @RequestParam Integer status) {
        return adminService.updateAdminStatus(id, status);
    }

    /**
     * 系统管理员重置指定管理员密码
     */
    @PutMapping("/{id}/password")
    public Result<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String newPassword = params.get("newPassword");
        return adminService.updateAdminPassword(id, newPassword);
    }

    // ========== 个人资料管理 ==========

    /**
     * 修改当前管理员个人资料
     */
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody Admin admin, HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        admin.setId(adminId);
        return adminService.updateAdmin(admin);
    }

    /**
     * 上传管理员头像图片
     */
    @PostMapping("/avatar")
    public Result<?> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");

        if (file.isEmpty()) {
            return Result.error("请选择文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只支持图片文件");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String fileName = "admin_avatar_" + adminId + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            String avatarUrl = "/images/" + fileName;
            Admin admin = new Admin();
            admin.setId(adminId);
            admin.setAvatar(avatarUrl);
            adminService.updateAdmin(admin);

            return Result.success(avatarUrl);
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 当前管理员修改自己的密码（需验证旧密码）
     */
    @PutMapping("/password")
    public Result<?> updateMyPassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        return adminService.updateMyPassword(adminId, oldPassword, newPassword);
    }
}
