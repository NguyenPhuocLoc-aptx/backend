
package com.zosh.service;

import com.zosh.exception.MailsException;
import com.zosh.model.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {

	void sendInvitation(String email, String projectId) throws MailsException, MessagingException; // ✅ Long → String

	Invitation acceptInvitation(String token, String userId) throws Exception; // ✅ Long → String

	String getTokenByUserMail(String userEmail);

	void deleteToken(String token);
}