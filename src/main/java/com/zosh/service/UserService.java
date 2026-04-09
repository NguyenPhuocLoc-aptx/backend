package com.zosh.service;

import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.User;

public interface UserService {

	User findUserProfileByJwt(String jwt) throws UserException, ProjectException;

	User findUserByEmail(String email) throws UserException;

	User findUserById(String userId) throws UserException;  // ✅ Long → String

	User updateUsersProjectSize(User user, int number);

	void updatePassword(User user, String newPassword);

	void sendPasswordResetEmail(User user);
}