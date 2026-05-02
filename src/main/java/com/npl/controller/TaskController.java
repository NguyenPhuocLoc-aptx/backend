package com.npl.controller;

import com.npl.dto.request.CreateTaskRequest;
import com.npl.exception.ProjectException;
import com.npl.exception.TaskException;
import com.npl.exception.UserException;
import com.npl.model.Task;
import com.npl.model.User;
import com.npl.service.TaskService;
import com.npl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String taskId) throws TaskException {
        return taskService.getIssueById(taskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable String projectId) throws ProjectException {
        return ResponseEntity.ok(taskService.getIssueByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest req, 
                                          @RequestHeader("Authorization") String jwt) 
            throws UserException, ProjectException, TaskException {
        User user = userService.findUserProfileByJwt(jwt);
        Task createdTask = taskService.createIssue(req, user.getId());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable String taskId, 
                                          @RequestBody CreateTaskRequest req,
                                          @RequestHeader("Authorization") String jwt) 
            throws TaskException, UserException, ProjectException {
        User user = userService.findUserProfileByJwt(jwt);
        return taskService.updateIssue(taskId, req, user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId, 
                                            @RequestHeader("Authorization") String jwt) 
            throws UserException, TaskException {
        User user = userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(taskService.deleteIssue(taskId, user.getId()));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<Task>> getMyTasks(@RequestHeader("Authorization") String jwt) 
            throws UserException, TaskException, ProjectException {
        User user = userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(taskService.getIssuesByAssigneeId(user.getId()));
    }

    @PatchMapping("/{taskId}/status/{status}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable String taskId, @PathVariable String status) 
            throws TaskException {
        return ResponseEntity.ok(taskService.updateStatus(taskId, status));
    }

    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable String taskId, @PathVariable String userId) 
            throws UserException, TaskException {
        return ResponseEntity.ok(taskService.addUserToIssue(taskId, userId));
    }
}