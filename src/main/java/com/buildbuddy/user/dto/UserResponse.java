
// UserResponse.java
package com.buildbuddy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String bio;
    private List<String> skills;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String resumeUrl;
    private String profilePictureUrl;
    private String role;
    private boolean verified;
    private LocalDateTime createdAt;
}