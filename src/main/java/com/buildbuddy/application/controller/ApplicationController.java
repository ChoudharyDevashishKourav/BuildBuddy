package com.buildbuddy.application.controller;

import com.buildbuddy.application.dto.ApplicationResponse;
import com.buildbuddy.application.dto.CreateApplicationRequest;
import com.buildbuddy.application.dto.UpdateApplicationRequest;
import com.buildbuddy.application.service.ApplicationService;
import com.buildbuddy.common.dto.ApiResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ApplicationResponse>> createApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        ApplicationResponse application = applicationService.createApplication(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted successfully", application));
    }
    
    @GetMapping("/team/{teamId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getTeamApplications(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<ApplicationResponse> applications = applicationService.getApplicationsForTeam(teamId, userId);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        ApplicationResponse application = applicationService.updateApplicationStatus(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Application status updated", application));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getUserApplications(
            @PathVariable UUID userId) {
        List<ApplicationResponse> applications = applicationService.getUserApplications(userId);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }
}