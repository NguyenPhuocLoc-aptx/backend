package com.npl.dto.request;
import lombok.Data;

@Data
public class InvitationRequest {
    private String email;
    private String projectId;
}