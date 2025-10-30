
// TeamPostResponse.java
package com.buildbuddy.team.dto;

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
public class TeamPostResponse {
    private Long id;
    private String title;
    private String description;
    private String hackathonName;
    private List<String> requiredSkills;
    private String experienceLevel;
    private String mode;
    private Integer memberLimit;
    private CreatorInfo creator;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatorInfo {
        private UUID id;
        private String name;
        private String email;
        private String profilePictureUrl;
    }
}