package com.buildbuddy.team.service;

import com.buildbuddy.common.exception.ResourceNotFoundException;
import com.buildbuddy.common.exception.UnauthorizedException;
import com.buildbuddy.team.dto.CreateTeamRequest;
import com.buildbuddy.team.dto.TeamPostResponse;
import com.buildbuddy.team.entity.ExperienceLevel;
import com.buildbuddy.team.entity.TeamMode;
import com.buildbuddy.team.entity.TeamPost;
import com.buildbuddy.team.repository.TeamPostRepository;
import com.buildbuddy.user.entity.User;
import com.buildbuddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TeamPostRepository teamPostRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public TeamPostResponse createTeam(CreateTeamRequest request, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        TeamPost teamPost = TeamPost.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .hackathonName(request.getHackathonName())
                .requiredSkills(request.getRequiredSkills())
                .experienceLevel(ExperienceLevel.valueOf(request.getExperienceLevel().toUpperCase()))
                .mode(TeamMode.valueOf(request.getMode().toUpperCase()))
                .memberLimit(request.getMemberLimit())
                .creator(creator)
                .build();
        
        TeamPost saved = teamPostRepository.save(teamPost);
        return mapToResponse(saved);
    }
    
    public Page<TeamPostResponse> getAllTeams(List<String> skills, String mode, 
                                               String experienceLevel, Pageable pageable) {
        Page<TeamPost> teams = teamPostRepository.findByFilters(skills, mode, experienceLevel, pageable);
        return teams.map(this::mapToResponse);
    }
    
    public TeamPostResponse getTeamById(Long id) {
        TeamPost team = teamPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team post not found"));
        return mapToResponse(team);
    }
    
    @Transactional
    public TeamPostResponse updateTeam(Long id, CreateTeamRequest request, UUID userId) {
        TeamPost team = teamPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team post not found"));
        
        if (!team.getCreator().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this team");
        }
        
        team.setTitle(request.getTitle());
        team.setDescription(request.getDescription());
        team.setHackathonName(request.getHackathonName());
        team.setRequiredSkills(request.getRequiredSkills());
        team.setExperienceLevel(ExperienceLevel.valueOf(request.getExperienceLevel().toUpperCase()));
        team.setMode(TeamMode.valueOf(request.getMode().toUpperCase()));
        team.setMemberLimit(request.getMemberLimit());
        
        TeamPost updated = teamPostRepository.save(team);
        return mapToResponse(updated);
    }
    
    @Transactional
    public void deleteTeam(Long id, UUID userId) {
        TeamPost team = teamPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team post not found"));
        
        if (!team.getCreator().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this team");
        }
        
        teamPostRepository.delete(team);
    }
    
    private TeamPostResponse mapToResponse(TeamPost team) {
        User creator = team.getCreator();
        return TeamPostResponse.builder()
                .id(team.getId())
                .title(team.getTitle())
                .description(team.getDescription())
                .hackathonName(team.getHackathonName())
                .requiredSkills(team.getRequiredSkills())
                .experienceLevel(team.getExperienceLevel().name())
                .mode(team.getMode().name())
                .memberLimit(team.getMemberLimit())
                .creator(TeamPostResponse.CreatorInfo.builder()
                        .id(creator.getId())
                        .name(creator.getName())
                        .email(creator.getEmail())
                        .profilePictureUrl(creator.getProfilePictureUrl())
                        .build())
                .createdAt(team.getCreatedAt())
                .build();
    }
}