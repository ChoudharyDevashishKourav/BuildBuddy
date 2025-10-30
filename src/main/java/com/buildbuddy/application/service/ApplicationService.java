package com.buildbuddy.application.service;

import com.buildbuddy.application.dto.ApplicationResponse;
import com.buildbuddy.application.dto.CreateApplicationRequest;
import com.buildbuddy.application.dto.UpdateApplicationRequest;
import com.buildbuddy.application.entity.Application;
import com.buildbuddy.application.entity.ApplicationStatus;
import com.buildbuddy.application.repository.ApplicationRepository;
import com.buildbuddy.common.exception.BadRequestException;
import com.buildbuddy.common.exception.ResourceNotFoundException;
import com.buildbuddy.common.exception.UnauthorizedException;
import com.buildbuddy.team.entity.TeamPost;
import com.buildbuddy.team.repository.TeamPostRepository;
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
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final TeamPostRepository teamPostRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest request, UUID applicantId) {
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        TeamPost teamPost = teamPostRepository.findById(request.getTeamPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Team post not found"));
        
        if (teamPost.getCreator().getId().equals(applicantId)) {
            throw new BadRequestException("Cannot apply to your own team");
        }
        
        if (applicationRepository.existsByApplicantIdAndTeamPostId(applicantId, request.getTeamPostId())) {
            throw new BadRequestException("You have already applied to this team");
        }
        
        Application application = Application.builder()
                .applicant(applicant)
                .teamPost(teamPost)
                .githubUrl(request.getGithubUrl())
                .linkedinUrl(request.getLinkedinUrl())
                .resumeUrl(request.getResumeUrl())
                .status(ApplicationStatus.PENDING)
                .build();
        
        Application saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }
    
    public List<ApplicationResponse> getApplicationsForTeam(Long teamId, UUID userId) {
        TeamPost teamPost = teamPostRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team post not found"));
        
        if (!teamPost.getCreator().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view applications for this team");
        }
        
        List<Application> applications = applicationRepository.findByTeamPostId(teamId);
        return applications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationRequest request, UUID userId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        if (!application.getTeamPost().getCreator().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this application");
        }
        
        ApplicationStatus status = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
        application.setStatus(status);
        
        Application updated = applicationRepository.save(application);
        return mapToResponse(updated);
    }
    
    public List<ApplicationResponse> getUserApplications(UUID userId) {
        List<Application> applications = applicationRepository.findByApplicantId(userId);
        return applications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    private ApplicationResponse mapToResponse(Application application) {
        User applicant = application.getApplicant();
        TeamPost teamPost = application.getTeamPost();
        
        return ApplicationResponse.builder()
                .id(application.getId())
                .applicant(ApplicationResponse.ApplicantInfo.builder()
                        .id(applicant.getId())
                        .name(applicant.getName())
                        .email(applicant.getEmail())
                        .bio(applicant.getBio())
                        .profilePictureUrl(applicant.getProfilePictureUrl())
                        .build())
                .teamPost(ApplicationResponse.TeamInfo.builder()
                        .id(teamPost.getId())
                        .title(teamPost.getTitle())
                        .hackathonName(teamPost.getHackathonName())
                        .build())
                .githubUrl(application.getGithubUrl())
                .linkedinUrl(application.getLinkedinUrl())
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus().name())
                .createdAt(application.getCreatedAt())
                .build();
    }
}