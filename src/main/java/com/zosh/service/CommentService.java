package com.zosh.service;

import com.zosh.exception.TaskException;
import com.zosh.exception.UserException;
import com.zosh.model.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(String taskId, String userId, String content) throws UserException, TaskException;

    void deleteComment(String commentId, String userId) throws UserException, TaskException;

    List<Comment> findCommentsByTaskId(String taskId) throws TaskException;
}