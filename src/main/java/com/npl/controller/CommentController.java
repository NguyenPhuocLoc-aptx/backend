package com.npl.controller;

import com.npl.exception.ProjectException;
import com.npl.exception.TaskException; // Đã đổi từ IssueException sang TaskException
import com.npl.exception.UserException;
import com.npl.model.Comment;
import com.npl.model.User;
import com.npl.request.CreateCommentRequest;
import com.npl.response.MessageResponse;
import com.npl.service.CommentService;
import com.npl.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CreateCommentRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException, TaskException, ProjectException {

        User user = userService.findUserProfileByJwt(jwt);
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

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(
            @PathVariable String taskId) throws TaskException {

        List<Comment> comments = commentService.findCommentsByTaskId(taskId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}