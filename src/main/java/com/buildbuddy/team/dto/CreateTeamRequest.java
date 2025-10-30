// CreateTeamRequest.java
package com.buildbuddy.team.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateTeamRequest {
    
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
    private String description;
    
    private String hackathonName;
    
    @NotEmpty(message = "At least one skill is required")
    private List<String> requiredSkills;
    
    @NotNull(message = "Experience level is required")
    private String experienceLevel;
    
    @NotNull(message = "Mode is required")
    private String mode;
    
    @Min(value = 1, message = "Member limit must be at least 1")
    @Max(value = 50, message = "Member limit cannot exceed 50")
    private Integer memberLimit;
}
