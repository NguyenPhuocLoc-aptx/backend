package com.zosh.request;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String taskId;
    private String content;
}