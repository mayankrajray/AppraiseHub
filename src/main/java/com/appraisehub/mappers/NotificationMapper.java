package com.appraisehub.mappers;

import com.appraisehub.dto.NotificationResponseDTO;
import com.appraisehub.entity.Notification;

public class NotificationMapper {

    public static NotificationResponseDTO toResponse(Notification notification) {
        if (notification == null) return null;

        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getId());
            dto.setUserName(notification.getUser().getFullName());
        }

        return dto;
    }
}