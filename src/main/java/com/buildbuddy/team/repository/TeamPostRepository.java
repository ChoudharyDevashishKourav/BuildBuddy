package com.buildbuddy.team.repository;

import com.buildbuddy.team.entity.TeamPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamPostRepository extends JpaRepository<TeamPost, Long> {
    
    @Query("SELECT t FROM TeamPost t WHERE " +
           "(:skills IS NULL OR EXISTS (SELECT s FROM t.requiredSkills s WHERE s IN :skills)) AND " +
           "(:mode IS NULL OR t.mode = :mode) AND " +
           "(:experienceLevel IS NULL OR t.experienceLevel = :experienceLevel)")
    Page<TeamPost> findByFilters(
            @Param("skills") List<String> skills,
            @Param("mode") String mode,
            @Param("experienceLevel") String experienceLevel,
            Pageable pageable);
    
    Page<TeamPost> findByCreatorId(UUID creatorId, Pageable pageable);
}