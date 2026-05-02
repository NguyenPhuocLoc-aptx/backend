package com.npl.dto.request;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectCreateRequest {
    private String name;
    private String description;
    private String category;
    private String workspaceId;
    private com.npl.enums.ProjectStatus status;
    private com.npl.enums.Priority priority;
    private Integer progress;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}