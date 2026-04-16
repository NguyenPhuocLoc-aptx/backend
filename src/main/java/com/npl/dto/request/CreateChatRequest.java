package com.npl.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {

	private Long projectId;
	
	private List<Long> userIds;
}
