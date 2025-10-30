// UpdateUserRequest.java
package com.buildbuddy.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {
    
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
    
    private List<String> skills;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String profilePictureUrl;
}
