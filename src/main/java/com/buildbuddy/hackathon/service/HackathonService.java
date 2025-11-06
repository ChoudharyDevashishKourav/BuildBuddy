package com.buildbuddy.hackathon.service;

import com.buildbuddy.hackathon.dto.HackathonResponse;
import com.buildbuddy.hackathon.entity.Hackathon;
import com.buildbuddy.hackathon.entity.Platform;
import com.buildbuddy.hackathon.repository.HackathonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HackathonService {
    
    private final HackathonRepository hackathonRepository;
    
    public Page<HackathonResponse> getAllHackathons(String platform, Pageable pageable) {
        Page<Hackathon> hackathons;
        
        if (platform != null && !platform.isEmpty()) {
            hackathons = hackathonRepository.findByPlatform(Platform.valueOf(platform.toUpperCase()), pageable);
        } else {
            hackathons = hackathonRepository.findAll(pageable);
        }
        
        return hackathons.map(this::mapToResponse);
    }
    
    private HackathonResponse mapToResponse(Hackathon hackathon) {
        return HackathonResponse.builder()
                .id(hackathon.getId())
                .title(hackathon.getTitle())
                .platform(hackathon.getPlatform().name())
                .link(hackathon.getLink())
                .mode(hackathon.getMode() != null ? hackathon.getMode().name() : null)
                .location(hackathon.getLocation())
                .startDate(hackathon.getStartDate())
                .endDate(hackathon.getEndDate())
                .status(hackathon.getStatus() != null ? hackathon.getStatus().name() : null)
                .createdAt(hackathon.getCreatedAt())
                .build();
    }
}