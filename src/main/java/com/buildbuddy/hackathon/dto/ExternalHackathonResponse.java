package com.buildbuddy.hackathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalHackathonResponse {
    private String id;
    private String title;
    private String platform;
    private String link;
    private String mode;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String description;
    private Integer registrationsCount;
    private String prizeAmount;
    private String thumbnailUrl;
    private String organizationName;
}
