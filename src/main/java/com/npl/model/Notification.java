package com.npl.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User user;

    private String message;
    private Boolean isRead = false;
    
    @Enumerated(EnumType.STRING)
    private com.npl.enums.NotificationType type; 
    
    private LocalDateTime createdAt;
}