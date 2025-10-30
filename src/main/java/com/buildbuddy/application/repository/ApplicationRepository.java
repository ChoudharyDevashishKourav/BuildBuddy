package com.buildbuddy.application.repository;

import com.buildbuddy.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByTeamPostId(Long teamPostId);
    List<Application> findByApplicantId(UUID applicantId);
    boolean existsByApplicantIdAndTeamPostId(UUID applicantId, Long teamPostId);
}