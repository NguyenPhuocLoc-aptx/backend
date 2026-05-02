package com.npl.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String id;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
}