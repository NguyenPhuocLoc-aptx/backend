package com.zosh.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.zosh.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zosh.config.JwtProvider;
import com.zosh.exception.UserException;
import com.zosh.model.PasswordResetToken;
import com.zosh.model.User;
import com.zosh.repository.PasswordResetTokenRepository;
import com.zosh.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException, ProjectException {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		// ✅ Fixed: null check before save; use Optional from findByEmail
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserException("User not found with email " + email));
	}

	@Override
	public User findUserByEmail(String username) throws UserException {
		// ✅ Fixed: findByEmail returns Optional<User>
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UserException("User not found with email " + username));
	}

	@Override
	public User findUserById(String userId) throws UserException {  // ✅ Long → String
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with id " + userId));
	}

	@Override
	public User updateUsersProjectSize(User user, int number) {
		// User model no longer has projectSize — this is a no-op kept for compatibility
		return userRepository.save(user);
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	@Override
	public void sendPasswordResetEmail(User user) {
		String resetToken = UUID.randomUUID().toString();
		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);


		PasswordResetToken passwordResetToken = PasswordResetToken.builder()
				.token(resetToken)
				.user(user)
				.expiresAt(expiresAt)
				.used(false)
				.build();
		passwordResetTokenRepository.save(passwordResetToken);

		sendEmail(user.getEmail(), "Password Reset",
				"Click the following link to reset your password: http://localhost:5454/reset-password?token=" + resetToken);
	}

	private void sendEmail(String to, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);
	}
}