package com.zosh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.PasswordResetToken;
import com.zosh.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenServiceImplementation implements PasswordResetTokenService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public PasswordResetToken findByToken(String token) {
		// ✅ Fixed: findByToken returns Optional<PasswordResetToken>
		return passwordResetTokenRepository.findByToken(token).orElse(null);
	}

	@Override
	public void delete(PasswordResetToken resetToken) {
		passwordResetTokenRepository.delete(resetToken);
	}
}