package com.buildbuddy.favorite.controller;

import com.buildbuddy.common.dto.ApiResponse;
import com.buildbuddy.favorite.dto.FavoriteResponse;
import com.buildbuddy.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @PostMapping("/{hackathonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addFavorite(
            @PathVariable Long hackathonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        FavoriteResponse favorite = favoriteService.addFavorite(hackathonId, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Added to favorites", favorite));
    }
    
    @DeleteMapping("/{hackathonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @PathVariable Long hackathonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        favoriteService.removeFavorite(hackathonId, userId);
        return ResponseEntity.ok(ApiResponse.success("Removed from favorites", null));
    }
    
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getUserFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<FavoriteResponse> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
}