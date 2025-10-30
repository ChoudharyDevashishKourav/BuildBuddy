package com.buildbuddy.team.controller;

import com.buildbuddy.common.dto.ApiResponse;
import com.buildbuddy.team.dto.CreateTeamRequest;
import com.buildbuddy.team.dto.TeamPostResponse;
import com.buildbuddy.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TeamPostResponse>> createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        TeamPostResponse team = teamService.createTeam(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Team created successfully", team));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TeamPostResponse>>> getAllTeams(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<TeamPostResponse> teams = teamService.getAllTeams(skills, mode, experienceLevel, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamPostResponse>> getTeamById(@PathVariable Long id) {
        TeamPostResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success(team));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TeamPostResponse>> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody CreateTeamRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        TeamPostResponse team = teamService.updateTeam(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", team));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        teamService.deleteTeam(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Team deleted successfully", null));
    }
}