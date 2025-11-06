package com.buildbuddy.hackathon.controller;

import com.buildbuddy.common.dto.ApiResponse;
import com.buildbuddy.hackathon.dto.HackathonResponse;
import com.buildbuddy.hackathon.service.HackathonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hackathons")
@RequiredArgsConstructor
public class HackathonController {
    
    private final HackathonService hackathonService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<HackathonResponse>>> getAllHackathons(
            @RequestParam(required = false) String platform,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startDate") String sortBy) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<HackathonResponse> hackathons = hackathonService.getAllHackathons(platform, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(hackathons));
    }
    
    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> syncHackathons() {
        // Implement hackathon sync logic here (e.g., API scraping)
        return ResponseEntity.ok(ApiResponse.success("Hackathons synced successfully", null));
    }
}