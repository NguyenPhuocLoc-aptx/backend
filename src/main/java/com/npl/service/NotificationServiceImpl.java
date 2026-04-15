package com.npl.service;

import com.npl.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // FIX: This is the magic annotation that fixes the autowire error!
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    // private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getNotificationsForUser(String username) throws Exception {
        // TODO: Query the repository and map to NotificationResponse
        return null;
    }

    @Override
    public Long countUnread(String username) throws Exception {
        // TODO: Query the repository to count unread notifications
        return 0L;
    }

    @Override
    public void markAsRead(String id, String username) throws Exception {
        // TODO: Fetch by ID, set isRead to true, and save
    }

    @Override
    public void markAllAsRead(String username) throws Exception {
        // TODO: Fetch all unread for user, set isRead to true, and save
    }

    @Override
    public void deleteNotification(String id, String username) throws Exception {
        // TODO: Fetch by ID, verify ownership, and delete
    }
}