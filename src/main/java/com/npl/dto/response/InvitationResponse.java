package com.npl.dto.response;
import lombok.Data;

@Data
public class InvitationResponse {
    private String id;
    private String email;
    private String projectId;
    private String projectName;
    private String token;
}