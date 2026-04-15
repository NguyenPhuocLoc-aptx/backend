package com.npl.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {

	private String title;
	private String description;
	private String status;
	private String projectId;
	private String priority;
	private LocalDate dueDate;
	private String userId;     

}
