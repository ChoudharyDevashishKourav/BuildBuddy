// ApplicationResponse.java
package com.buildbuddy.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private ApplicantInfo applicant;
    private TeamInfo teamPost;
    private String githubUrl;
    private String linkedinUrl;
    private String resumeUrl;
    private String status;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantInfo {
        private UUID id;
        private String name;
        private String email;
        private String bio;
        private String profilePictureUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamInfo {
        private Long id;
        private String title;
        private String hackathonName;
    }
}