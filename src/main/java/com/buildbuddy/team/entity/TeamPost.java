// TeamPost.java
package com.buildbuddy.team.entity;

import com.buildbuddy.common.entity.BaseEntity;
import com.buildbuddy.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPost extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    private String hackathonName;
    
    @ElementCollection
    @CollectionTable(name = "team_required_skills", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    
    @Enumerated(EnumType.STRING)
    private TeamMode mode;
    
    private Integer memberLimit;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
}

