package com.npl.service;

import com.npl.dto.response.NotificationResponse;
import com.npl.model.Notification;
import com.npl.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getNotificationsForUser(String userId) {
        // FIXED: Matched 'findAllBy...' from repository
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUnread(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(String id, String userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));

        // FIXED: Use getUser().getId() because it is a ManyToOne relationship
        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to modify this notification");
        }

        // FIXED: Lombok generates setIsRead() for Boolean wrappers
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional // Required when using custom @Modifying queries
    @Override
    public void markAllAsRead(String userId) {
        // FIXED: Highly optimized! Instead of fetching everything and looping,
        // this simply runs the custom SQL UPDATE query you wrote in the repository.
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    public void deleteNotification(String id, String userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));

        // FIXED: Use getUser().getId()
        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this notification");
        }

        notificationRepository.delete(notification);
    }

    // --- HELPER METHOD ---

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();

        response.setId(notification.getId());
        response.setMessage(notification.getMessage());

        if (notification.getType() != null) {
            response.setType(notification.getType().name());
        }

        response.setRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}