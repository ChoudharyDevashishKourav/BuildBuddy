package com.buildbuddy.user.service;

import com.buildbuddy.common.exception.ResourceNotFoundException;
import com.buildbuddy.user.dto.UpdateUserRequest;
import com.buildbuddy.user.dto.UserResponse;
import com.buildbuddy.user.entity.User;
import com.buildbuddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponse(user);
    }
    
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (request.getName() != null) user.setName(request.getName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getSkills() != null) user.setSkills(request.getSkills());
        if (request.getGithubUrl() != null) user.setGithubUrl(request.getGithubUrl());
        if (request.getLinkedinUrl() != null) user.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getPortfolioUrl() != null) user.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());
        
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }
    
    @Transactional
    public String uploadResume(UUID userId, String resumeUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setResumeUrl(resumeUrl);
        userRepository.save(user);
        return resumeUrl;
    }
    
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .skills(user.getSkills())
                .githubUrl(user.getGithubUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .resumeUrl(user.getResumeUrl())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole().name())
                .verified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}