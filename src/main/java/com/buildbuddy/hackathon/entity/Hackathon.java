// Hackathon.java
package com.buildbuddy.hackathon.entity;

import com.buildbuddy.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hackathons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hackathon extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;
    
    @Column(nullable = false)
    private String link;
    
    @Enumerated(EnumType.STRING)
    private HackathonMode mode;
    
    private String location;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    private HackathonStatus status;
}

