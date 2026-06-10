package com.appraisehub.service;

import com.appraisehub.dto.NotificationRequestDTO;
import com.appraisehub.dto.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);
    List<NotificationResponseDTO> getNotificationsByUserId(Long userId);
    List<NotificationResponseDTO> getUnreadNotificationsByUserId(Long userId);
    NotificationResponseDTO markAsRead(Long id);
    void deleteNotification(Long id);
}