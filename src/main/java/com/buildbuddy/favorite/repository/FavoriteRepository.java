package com.buildbuddy.favorite.repository;

import com.buildbuddy.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(UUID userId);
    Optional<Favorite> findByUserIdAndHackathonId(UUID userId, Long hackathonId);
    boolean existsByUserIdAndHackathonId(UUID userId, Long hackathonId);
}