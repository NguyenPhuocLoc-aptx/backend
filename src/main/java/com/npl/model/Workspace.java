package com.npl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Workspace {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}