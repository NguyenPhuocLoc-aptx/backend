package com.zosh.service;

import java.util.List;
import java.util.Optional;

import com.zosh.exception.TaskException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Task;
import com.zosh.model.User;
import com.zosh.request.IssueRequest;

public interface IssueService {

	Optional<Task> getIssueById(String issueId) throws TaskException;           // ✅ Long → String, Issue → Task

	List<Task> getIssueByProjectId(String projectId) throws ProjectException;   // ✅

	Task createIssue(IssueRequest issue, String userId)                         // ✅
			throws UserException, TaskException, ProjectException;

	Optional<Task> updateIssue(String issueId, IssueRequest updatedIssue, String userId) // ✅
			throws TaskException, UserException, ProjectException;

	String deleteIssue(String issueId, String userId) throws UserException, TaskException; // ✅

	List<Task> getIssuesByAssigneeId(String assigneeId) throws TaskException;   // ✅

	List<Task> searchIssues(String title, String status, String priority, String assigneeId) // ✅
			throws TaskException;

	List<User> getAssigneeForIssue(String issueId) throws TaskException;        // ✅

	Task addUserToIssue(String issueId, String userId) throws UserException, TaskException; // ✅

	Task updateStatus(String issueId, String status) throws TaskException;      // ✅
}