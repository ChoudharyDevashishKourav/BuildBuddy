package com.buildbuddy.auth.service;

import com.buildbuddy.auth.dto.AuthResponse;
import com.buildbuddy.auth.dto.LoginRequest;
import com.buildbuddy.auth.dto.RegisterRequest;
import com.buildbuddy.common.exception.BadRequestException;
import com.buildbuddy.common.exception.ResourceNotFoundException;
import com.buildbuddy.common.exception.UnauthorizedException;
import com.buildbuddy.user.entity.User;
import com.buildbuddy.user.entity.UserRole;
import com.buildbuddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        
        String verificationToken = UUID.randomUUID().toString();
        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .verified(false)
                .verificationToken(verificationToken)
                .build();
        
        User savedUser = userRepository.save(user);
        
        // Send verification email
        try {
            emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole().name())
                .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
    
    @Transactional
    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));
        
        if (user.isVerified()) {
            throw new BadRequestException("Email already verified");
        }
        
        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        
        return "Email verified successfully";
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                userRepository.findById(UUID.fromString(username))
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                        .getEmail()
        );
        
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        
        String newToken = jwtService.generateToken(userDetails);
        User user = userRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}