package com.buildbuddy.hackathon.service;

import com.buildbuddy.hackathon.dto.ExternalHackathonResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HackathonScraperService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Fetch from Unstop API
    public List<ExternalHackathonResponse> fetchUnstopHackathons() {
        List<ExternalHackathonResponse> hackathons = new ArrayList<>();

        try {
            String url = "https://unstop.com/api/public/opportunity/search-result?opportunity=hackathons&oppstatus=recent&page=1";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.path("data").path("data");

            for (JsonNode node : dataArray) {
                try {
                    ExternalHackathonResponse hackathon = ExternalHackathonResponse.builder()
                            .id("unstop_" + node.path("id").asText())
                            .title(node.path("title").asText())
                            .platform("UNSTOP")
                            .link(node.path("seo_url").asText())
                            .mode(extractUnstopMode(node.path("region").asText()))
                            .location(node.path("region").asText())
                            .startDate(parseUnstopDate(node.path("start_date").asText()))
                            .endDate(parseUnstopDate(node.path("end_date").asText()))
                            .status(determineStatus(parseUnstopDate(node.path("start_date").asText()),
                                    parseUnstopDate(node.path("end_date").asText())))
                            .description(extractDescription(node))
                            .registrationsCount(node.path("registerCount").asInt(0))
                            .organizationName(node.path("organisation").path("name").asText())
                            .thumbnailUrl(node.path("banner_mobile").path("image_url").asText())
                            .prizeAmount(extractPrizeAmount(node))
                            .build();

                    hackathons.add(hackathon);
                } catch (Exception e) {
                    log.error("Error parsing Unstop hackathon: {}", e.getMessage());
                }
            }

            log.info("Fetched {} hackathons from Unstop", hackathons.size());
        } catch (Exception e) {
            log.error("Error fetching Unstop hackathons: {}", e.getMessage());
        }

        return hackathons;
    }

    // Fetch from Devpost API
    public List<ExternalHackathonResponse> fetchDevpostHackathons() {
        List<ExternalHackathonResponse> hackathons = new ArrayList<>();

        try {
            String url = "https://devpost.com/api/hackathons?order_by=recently-added&per_page=20";

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode hackathonsArray = root.path("hackathons");

            for (JsonNode node : hackathonsArray) {
                try {
                    ExternalHackathonResponse hackathon = ExternalHackathonResponse.builder()
                            .id("devpost_" + node.path("id").asText())
                            .title(node.path("title").asText())
                            .platform("DEVPOST")
                            .link(node.path("url").asText())
                            .mode(extractDevpostMode(node.path("displayed_location")))
                            .location(node.path("displayed_location").path("location").asText())
                            .startDate(null) // Devpost doesn't provide structured dates
                            .endDate(null)
                            .status(node.path("open_state").asText().equals("open") ? "OPEN" : "CLOSED")
                            .description(node.path("submission_period_dates").asText())
                            .registrationsCount(node.path("registrations_count").asInt(0))
                            .organizationName(node.path("organization_name").asText())
                            .thumbnailUrl(node.path("thumbnail_url").asText())
                            .prizeAmount(extractDevpostPrize(node))
                            .build();

                    hackathons.add(hackathon);
                } catch (Exception e) {
                    log.error("Error parsing Devpost hackathon: {}", e.getMessage());
                }
            }

            log.info("Fetched {} hackathons from Devpost", hackathons.size());
        } catch (Exception e) {
            log.error("Error fetching Devpost hackathons: {}", e.getMessage());
        }

        return hackathons;
    }

    // Fetch from Devfolio API
    public List<ExternalHackathonResponse> fetchDevfolioHackathons() {
        List<ExternalHackathonResponse> hackathons = new ArrayList<>();

        try {
            String url = "https://api.devfolio.co/api/search/hackathons";

            // Devfolio requires POST request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("type", "application_open");
            requestBody.put("size", 20);

            String response = restTemplate.postForObject(url, requestBody, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode hits = root.path("hits").path("hits");

            for (JsonNode node : hits) {
                try {
                    JsonNode source = node.path("_source");

                    ExternalHackathonResponse hackathon = ExternalHackathonResponse.builder()
                            .id("devfolio_" + node.path("_id").asText())
                            .title(source.path("name").asText())
                            .platform("DEVFOLIO")
                            .link("https://devfolio.co/hackathons/" + source.path("slug").asText())
                            .mode(extractDevfolioMode(source))
                            .location(extractDevfolioLocation(source))
                            .startDate(parseDevfolioDate(source.path("starts_at").asText()))
                            .endDate(parseDevfolioDate(source.path("ends_at").asText()))
                            .status(determineStatus(
                                    parseDevfolioDate(source.path("starts_at").asText()),
                                    parseDevfolioDate(source.path("ends_at").asText())))
                            .description(source.path("tagline").asText())
                            .registrationsCount(source.path("registrations_count").asInt(0))
                            .organizationName(source.path("name").asText())
                            .thumbnailUrl(source.path("coverImage").asText())
                            .prizeAmount(null) // Devfolio doesn't expose prize in API
                            .build();

                    hackathons.add(hackathon);
                } catch (Exception e) {
                    log.error("Error parsing Devfolio hackathon: {}", e.getMessage());
                }
            }

            log.info("Fetched {} hackathons from Devfolio", hackathons.size());
        } catch (Exception e) {
            log.error("Error fetching Devfolio hackathons: {}", e.getMessage());
        }

        return hackathons;
    }

    // ==================== HELPER METHODS ====================

    private String extractUnstopMode(String region) {
        if (region == null) return "Hybrid";
        region = region.toLowerCase();
        if (region.contains("online") || region.equals("online")) return "Online";
        if (region.contains("offline")) return "Offline";
        return "Hybrid";
    }

    private String extractDevpostMode(JsonNode location) {
        String loc = location.path("location").asText().toLowerCase();
        if (loc.contains("online")) return "Online";
        return "Offline";
    }

    private String extractDevfolioMode(JsonNode source) {
        String applyMode = source.path("apply_mode").asText();
        boolean isOnline = source.path("is_online").asBoolean();

        if ("both".equals(applyMode)) return "Hybrid";
        if (isOnline) return "Online";
        return "Offline";
    }

    private String extractDevfolioLocation(JsonNode source) {
        String city = source.path("city").asText();
        String location = source.path("location").asText();

        if (!city.isEmpty()) return city;
        if (!location.isEmpty()) return location;
        return "Online";
    }

    private LocalDateTime parseUnstopDate(String dateStr) {
        try {
            // Format: "2025-11-16T00:00:00+05:30"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseDevfolioDate(String dateStr) {
        try {
            // Format: ISO 8601
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    private String determineStatus(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) return "UNKNOWN";

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) return "UPCOMING";
        if (now.isAfter(endDate)) return "ENDED";
        return "ONGOING";
    }

    private String extractDescription(JsonNode node) {
        // Try to get meaningful description from Unstop
        StringBuilder desc = new StringBuilder();

        JsonNode filters = node.path("filters");
        if (filters.isArray() && filters.size() > 0) {
            desc.append("Categories: ");
            for (JsonNode filter : filters) {
                desc.append(filter.path("name").asText()).append(", ");
            }
        }

        return desc.length() > 0 ? desc.substring(0, desc.length() - 2) : "";
    }

    private String extractPrizeAmount(JsonNode node) {
        JsonNode prizes = node.path("prizes");
        if (prizes.isArray() && prizes.size() > 0) {
            JsonNode firstPrize = prizes.get(0);
            int cash = firstPrize.path("cash").asInt(0);
            if (cash > 0) {
                return "₹" + cash;
            }
        }
        return null;
    }

    private String extractDevpostPrize(JsonNode node) {
        String prizeHtml = node.path("prize_amount").asText();
        // Extract value from HTML like "$<span>10,000</span>"
        if (prizeHtml != null && !prizeHtml.isEmpty()) {
            return prizeHtml.replaceAll("<[^>]*>", "").trim();
        }
        return null;
    }
}