package com.buildbuddy.hackathon.service;

import com.buildbuddy.hackathon.dto.ExternalHackathonResponse;
import com.buildbuddy.hackathon.dto.HackathonResponse;
import com.buildbuddy.hackathon.entity.Hackathon;
import com.buildbuddy.hackathon.entity.HackathonMode;
import com.buildbuddy.hackathon.entity.HackathonStatus;
import com.buildbuddy.hackathon.entity.Platform;
import com.buildbuddy.hackathon.repository.HackathonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final HackathonScraperService scraperService;

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

    public Map<String, List<ExternalHackathonResponse>> fetchExternalHackathons() {
        Map<String, List<ExternalHackathonResponse>> result = new HashMap<>();

        // Fetch from all platforms in parallel using CompletableFuture
        // Todo: add parralel processing
        try {
            List<ExternalHackathonResponse> unstop = scraperService.fetchUnstopHackathons();
            List<ExternalHackathonResponse> devpost = scraperService.fetchDevpostHackathons();
            List<ExternalHackathonResponse> devfolio = scraperService.fetchDevfolioHackathons();

            result.put("unstop", unstop);
            result.put("devpost", devpost);
            result.put("devfolio", devfolio);

        } catch (Exception e) {
            log.error("Error fetching external hackathons: {}", e.getMessage());
        }

        return result;
    }

    @Transactional
    public String syncExternalHackathons() {
        Map<String, List<ExternalHackathonResponse>> externalHackathons = fetchExternalHackathons();
        int savedCount = 0;

        for (Map.Entry<String, List<ExternalHackathonResponse>> entry : externalHackathons.entrySet()) {
            for (ExternalHackathonResponse external : entry.getValue()) {
                try {
                    // Check if hackathon already exists
                    Optional<Hackathon> existing = hackathonRepository
                            .findByTitleAndPlatform(external.getTitle(),
                                    Platform.valueOf(external.getPlatform()));

                    if (existing.isEmpty()) {
                        Hackathon hackathon = Hackathon.builder()
                                .title(external.getTitle())
                                .platform(Platform.valueOf(external.getPlatform()))
                                .link(external.getLink())
                                .mode(external.getMode() != null ?
                                        HackathonMode.valueOf(external.getMode().toUpperCase()) : null)
                                .location(external.getLocation())
                                .startDate(external.getStartDate())
                                .endDate(external.getEndDate())
                                .status(external.getStatus() != null ?
                                        HackathonStatus.valueOf(external.getStatus()) : null)
                                .build();

                        hackathonRepository.save(hackathon);
                        savedCount++;
                    }
                } catch (Exception e) {
                    log.error("Error saving hackathon: {}", e.getMessage());
                }
            }
        }

        return "Synced " + savedCount + " new hackathons to database";
    }

}