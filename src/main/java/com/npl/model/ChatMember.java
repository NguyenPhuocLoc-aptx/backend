package com.npl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMember {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    private Chat chat;
    
    @ManyToOne
    private User user;
}