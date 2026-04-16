package com.npl.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.npl.enums.Priority;
import com.npl.enums.TaskStatus;
import com.npl.exception.TaskException;
import com.npl.exception.ProjectException;
import com.npl.exception.UserException;
import com.npl.model.Project;
import com.npl.model.Task;
import com.npl.model.User;
import com.npl.repository.TaskRepository;
import com.npl.dto.request.IssueRequest;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final UserService userService;
	private final ProjectService projectService;
	// FIXED: Inject the interface, not the Implementation class
	private final NotificationService notificationService;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, UserService userService,
						   ProjectService projectService, NotificationService notificationService) {
		this.taskRepository = taskRepository;
		this.userService = userService;
		this.projectService = projectService;
		this.notificationService = notificationService;
	}

	@Override
	public Optional<Task> getIssueById(String issueId) throws TaskException {
		return Optional.of(taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("No task found with id " + issueId)));
	}

	@Override
	public List<Task> getIssueByProjectId(String projectId) throws ProjectException {
		projectService.getProjectById(projectId);
		return taskRepository.findAllByProjectId(projectId);
	}

	@Override
	// FIXED: Removed 'TaskException' as it is never thrown here
	public Task createIssue(IssueRequest issueRequest, String userId) throws UserException, ProjectException {
		User user = getUserOrThrow(userId);
		Project project = projectService.getProjectById(issueRequest.getProjectId());

		Task task = Task.builder()
				.title(issueRequest.getTitle())
				.description(issueRequest.getDescription())
				.status(parseStatus(issueRequest.getStatus()))
				.priority(parsePriority(issueRequest.getPriority()))
				.dueDate(issueRequest.getDueDate() != null
						? issueRequest.getDueDate().atStartOfDay() : null)
				.project(project)
				.createdBy(user)
				.build();

		return taskRepository.save(task);
	}

	@Override
	// FIXED: Removed 'ProjectException' as it is never thrown here
	public Optional<Task> updateIssue(String issueId, IssueRequest updatedIssue, String userId)
			throws TaskException, UserException {
		getUserOrThrow(userId);
		Task task = taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("Task not found with id " + issueId));

		if (updatedIssue.getTitle() != null)       task.setTitle(updatedIssue.getTitle());
		if (updatedIssue.getDescription() != null) task.setDescription(updatedIssue.getDescription());
		if (updatedIssue.getDueDate() != null)     task.setDueDate(updatedIssue.getDueDate().atStartOfDay());
		if (updatedIssue.getPriority() != null)    task.setPriority(parsePriority(updatedIssue.getPriority()));
		if (updatedIssue.getStatus() != null)      task.setStatus(parseStatus(updatedIssue.getStatus()));

		if (updatedIssue.getUserId() != null) {
			User assignee = userService.findUserById(updatedIssue.getUserId());
			task.setAssignee(assignee);
		}

		return Optional.of(taskRepository.save(task));
	}

	@Override
	public String deleteIssue(String issueId, String userId) throws UserException, TaskException {
		getUserOrThrow(userId);
		taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("Task not found with id " + issueId));
		taskRepository.deleteById(issueId);
		return "Task with id " + issueId + " deleted";
	}

	@Override
	public List<Task> getIssuesByAssigneeId(String assigneeId) throws TaskException {
		List<Task> tasks = taskRepository.findAllByAssigneeId(assigneeId);
		if (tasks == null || tasks.isEmpty()) throw new TaskException("No tasks found for assignee " + assigneeId);
		return tasks;
	}

	@Override
	// FIXED: Removed 'TaskException' as it is never thrown here
	public List<Task> searchIssues(String title, String status, String priority, String assigneeId) {
		List<Task> all = taskRepository.findAll();
		return all.stream()
				.filter(t -> title == null || (t.getTitle() != null && t.getTitle().contains(title)))
				.filter(t -> status == null || t.getStatus().name().equalsIgnoreCase(status))
				.filter(t -> priority == null || t.getPriority().name().equalsIgnoreCase(priority))
				.filter(t -> assigneeId == null || (t.getAssignee() != null && t.getAssignee().getId().equals(assigneeId)))
				.collect(Collectors.toList());
	}

	@Override
	public List<User> getAssigneeForIssue(String issueId) throws TaskException {
		Task task = taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("Task not found with id " + issueId));

		User assignee = task.getAssignee();
		if (assignee == null) {
			return Collections.emptyList();
		}
		return List.of(assignee);
	}

	@Override
	public Task addUserToIssue(String issueId, String userId) throws UserException, TaskException {
		User user = userService.findUserById(userId);
		Task task = taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("Task not found with id " + issueId));
		task.setAssignee(user);

		// FIXED: Update this to match your NotificationService structure and database schema.
		// Example of what it should look like:
		// notificationService.createNotification(user.getId(), "TASK", task.getId(), "TASK_ASSIGNED", "A new task has been assigned to you.");

		return taskRepository.save(task);
	}

	@Override
	public Task updateStatus(String issueId, String status) throws TaskException {
		Task task = taskRepository.findById(issueId)
				.orElseThrow(() -> new TaskException("Task not found with id " + issueId));
		task.setStatus(parseStatus(status));
		return taskRepository.save(task);
	}

	private User getUserOrThrow(String userId) throws UserException {
		return userService.findUserById(userId);
	}

	private TaskStatus parseStatus(String status) {
		try {
			return status != null ? TaskStatus.valueOf(status.toUpperCase()) : TaskStatus.TODO;
		} catch (IllegalArgumentException e) {
			return TaskStatus.TODO;
		}
	}

	private Priority parsePriority(String priority) {
		try {
			return priority != null ? Priority.valueOf(priority.toUpperCase()) : Priority.MEDIUM;
		} catch (IllegalArgumentException e) {
			return Priority.MEDIUM;
		}
	}
}