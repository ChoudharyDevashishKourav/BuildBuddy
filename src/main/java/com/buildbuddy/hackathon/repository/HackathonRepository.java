package com.buildbuddy.hackathon.repository;

import com.buildbuddy.hackathon.entity.Hackathon;
import com.buildbuddy.hackathon.entity.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {
    Page<Hackathon> findByPlatform(Platform platform, Pageable pageable);
    Optional<Hackathon> findByTitleAndPlatform(String title, Platform platform);
}