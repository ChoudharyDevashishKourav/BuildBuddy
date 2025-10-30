package com.buildbuddy.user.controller;

import com.buildbuddy.common.dto.ApiResponse;
import com.buildbuddy.config.CloudinaryService;
import com.buildbuddy.user.dto.UpdateUserRequest;
import com.buildbuddy.user.dto.UserResponse;
import com.buildbuddy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isOwnerOrAdmin(#id)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", user));
    }
    
    @PostMapping("/upload-resume")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId) {
        String resumeUrl = cloudinaryService.uploadFile(file, "resumes");
        String savedUrl = userService.uploadResume(userId, resumeUrl);
        return ResponseEntity.ok(ApiResponse.success("Resume uploaded successfully", savedUrl));
    }
}