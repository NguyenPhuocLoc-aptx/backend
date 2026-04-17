package com.npl.dto.request;

import java.time.LocalDateTime; // FIXED: Imported LocalDateTime instead of LocalDate

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
	private String title;
	private String description;
	private String status;
	private String projectId;
	private String priority;
	private LocalDateTime dueDate;
	private String assigneeId;
}