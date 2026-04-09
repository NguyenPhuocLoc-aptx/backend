package com.zosh.controller;

import com.zosh.exception.ProjectException;
import com.zosh.exception.TaskException; // Đã đổi từ IssueException sang TaskException
import com.zosh.exception.UserException;
import com.zosh.model.Comment;
import com.zosh.model.User;
import com.zosh.request.CreateCommentRequest;
import com.zosh.response.MessageResponse;
import com.zosh.service.CommentService;
import com.zosh.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor // Tự động tạo constructor cho các biến final, chuẩn Spring Boot mới
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CreateCommentRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException, TaskException, ProjectException {

        User user = userService.findUserProfileByJwt(jwt);
        // Gọi service tạo comment (nhớ đảm bảo DTO CreateCommentRequest đã đổi thành taskId)
        Comment createdComment = commentService.createComment(req.getTaskId(), user.getId(), req.getContent());

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(
            @PathVariable String commentId,
            @RequestHeader("Authorization") String jwt) throws UserException, TaskException, ProjectException {

        User user = userService.findUserProfileByJwt(jwt);
        commentService.deleteComment(commentId, user.getId());

        MessageResponse res = new MessageResponse();
        res.setMessage("Comment deleted successfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // Đổi endpoint để tránh xung đột với DELETE /{commentId}
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(
            @PathVariable String taskId) throws TaskException {

        List<Comment> comments = commentService.findCommentsByTaskId(taskId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}