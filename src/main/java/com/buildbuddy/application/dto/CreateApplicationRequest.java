// CreateApplicationRequest.java
package com.buildbuddy.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateApplicationRequest {
    
    @NotNull(message = "Team post ID is required")
    private Long teamPostId;
    
    private String githubUrl;
    private String linkedinUrl;
    private String resumeUrl;
}



