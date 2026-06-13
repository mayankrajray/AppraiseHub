package com.appraisehub.dto;

import com.appraisehub.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String message;
    private Notification.Type type;
    private boolean isRead;
    private LocalDateTime createdAt;
}