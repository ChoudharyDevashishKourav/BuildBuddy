package com.buildbuddy.favorite.service;

import com.buildbuddy.common.exception.BadRequestException;
import com.buildbuddy.common.exception.ResourceNotFoundException;
import com.buildbuddy.favorite.dto.FavoriteResponse;
import com.buildbuddy.favorite.entity.Favorite;
import com.buildbuddy.favorite.repository.FavoriteRepository;
import com.buildbuddy.hackathon.dto.HackathonResponse;
import com.buildbuddy.hackathon.entity.Hackathon;
import com.buildbuddy.hackathon.repository.HackathonRepository;
import com.buildbuddy.user.entity.User;
import com.buildbuddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;

    @Transactional
    public FavoriteResponse addFavorite(Long hackathonId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new ResourceNotFoundException("Hackathon not found"));

        if (favoriteRepository.existsByUserIdAndHackathonId(userId, hackathonId)) {
            throw new BadRequestException("Hackathon already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .hackathon(hackathon)
                .build();

        Favorite saved = favoriteRepository.save(favorite);
        return mapToResponse(saved);
    }

    @Transactional
    public void removeFavorite(Long hackathonId, UUID userId) {
        Favorite favorite = favoriteRepository.findByUserIdAndHackathonId(userId, hackathonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    public List<FavoriteResponse> getUserFavorites(UUID userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private FavoriteResponse mapToResponse(Favorite favorite) {
        Hackathon hackathon = favorite.getHackathon();

        HackathonResponse hackathonResponse = HackathonResponse.builder()
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

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .hackathon(hackathonResponse)
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}