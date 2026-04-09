package com.zosh.service;

import com.zosh.exception.MailsException;
import com.zosh.model.Invitation;
import com.zosh.model.Project;
import com.zosh.repository.InvitationRepository;
import com.zosh.repository.ProjectRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InviteTokenServiceImpl implements InvitationService {

	@Autowired
	private InvitationRepository invitationRepository;  // ✅ InviteTokenRepository → InvitationRepository

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public void sendInvitation(String email, String projectId)  // ✅ Long → String
			throws MailsException, MessagingException {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new MailsException("Project not found with id " + projectId));

		String invitationToken = UUID.randomUUID().toString();

		// ✅ Invitation.project is now a @ManyToOne relation, not a projectId field
		Invitation invitation = Invitation.builder()
				.email(email)
				.project(project)
				.token(invitationToken)
				.expiresAt(LocalDateTime.now().plusDays(7))
				.build();

		invitationRepository.save(invitation);

		String invitationLink = "http://localhost:5173/accept_invitation?token=" + invitationToken;
		emailService.sendEmailWithToken(email, invitationLink);
	}

	@Override
	public Invitation acceptInvitation(String token, String userId) throws Exception {  // ✅ Long → String
		Invitation invitation = invitationRepository.findByToken(token)
				.orElseThrow(() -> new Exception("Invalid invitation token"));

		if (invitation.isExpired()) {
			throw new Exception("Invitation token has expired");
		}

		return invitation;
	}

	@Override
	public void deleteToken(String token) {
		invitationRepository.findByToken(token).ifPresent(invitationRepository::delete);
	}

	@Override
	public String getTokenByUserMail(String userEmail) {
		return invitationRepository.findAllByEmailAndStatus(userEmail,
						com.zosh.enums.InvitationStatus.PENDING)
				.stream()
				.findFirst()
				.map(Invitation::getToken)
				.orElse(null);
	}
}