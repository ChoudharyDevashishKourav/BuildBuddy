// Application.java
package com.buildbuddy.application.entity;

import com.buildbuddy.application.entity.ApplicationStatus;
import com.buildbuddy.common.entity.BaseEntity;
import com.buildbuddy.team.entity.TeamPost;
import com.buildbuddy.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_post_id", nullable = false)
    private TeamPost teamPost;
    
    private String githubUrl;
    private String linkedinUrl;
    private String resumeUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.buildbuddy.application.entity.ApplicationStatus status = ApplicationStatus.PENDING;
}

