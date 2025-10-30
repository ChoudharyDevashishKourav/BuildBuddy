// SecurityService.java
package com.buildbuddy.config;

import com.buildbuddy.user.entity.User;
import com.buildbuddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public boolean isOwnerOrAdmin(UUID resourceOwnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UUID currentUserId = UUID.fromString(authentication.getName());
        User currentUser = userRepository.findById(currentUserId).orElse(null);

        if (currentUser == null) {
            return false;
        }

        return currentUser.getId().equals(resourceOwnerId) ||
                currentUser.getRole().name().equals("ADMIN");
    }
}

