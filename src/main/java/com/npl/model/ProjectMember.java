package com.npl.model;

import com.npl.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectMember {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    private Project project;
    
    @ManyToOne
    private User user;
    
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
}