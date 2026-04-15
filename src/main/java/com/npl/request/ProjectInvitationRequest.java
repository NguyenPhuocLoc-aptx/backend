package com.npl.request;

import lombok.Data;

@Data
public class ProjectInvitationRequest {
    private String projectId;
    private String email;
}
