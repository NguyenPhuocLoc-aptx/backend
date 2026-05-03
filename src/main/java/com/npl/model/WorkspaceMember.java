package com.npl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkspaceMember {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    private Workspace workspace;
    
    @ManyToOne
    private User user;
}