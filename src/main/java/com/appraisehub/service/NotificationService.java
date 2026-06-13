package com.appraisehub.service;

import com.appraisehub.dto.NotificationRequestDTO;
import com.appraisehub.dto.NotificationResponseDTO;
import com.appraisehub.entity.Notification;

import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);
    void send(Long userId, String title, String message,
              Notification.Type type, Object payload);
    List<NotificationResponseDTO> getNotificationsByUserId(Long userId);
    List<NotificationResponseDTO> getUnreadNotificationsByUserId(Long userId);
    NotificationResponseDTO markAsRead(Long id);
    void deleteNotification(Long id);
}