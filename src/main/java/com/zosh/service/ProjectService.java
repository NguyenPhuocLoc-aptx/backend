package com.zosh.service;

import java.util.List;

import com.zosh.exception.ChatException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Chat;
import com.zosh.model.Project;
import com.zosh.model.User;

public interface ProjectService {

	// Changed Long to String
	Project createProject(Project project, String userId) throws UserException;

//  List<Project> getProjectsByOwner(User owner) throws ProjectException;

	List<Project> getProjectsByTeam(User user,String category,String tag) throws ProjectException;

	// Changed Long to String
	Project getProjectById(String projectId) throws ProjectException;

	// Changed Longs to Strings
	String deleteProject(String projectId, String userId) throws UserException;

	// Changed Long to String
	Project updateProject(Project updatedProject, String id) throws ProjectException;

	List<Project> searchProjects(String keyword, User user) throws ProjectException;

	// Changed Longs to Strings
	void addUserToProject(String projectId, String userId) throws UserException, ProjectException;

	// Changed Longs to Strings
	void removeUserFromProject(String projectId, String userId) throws UserException, ProjectException;

	// Changed Long to String
	Chat getChatByProjectId(String projectId) throws ProjectException, ChatException;

}