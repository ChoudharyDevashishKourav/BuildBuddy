package com.buildbuddy.favorite.dto;

import com.buildbuddy.hackathon.dto.HackathonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private HackathonResponse hackathon;
    private LocalDateTime createdAt;
}