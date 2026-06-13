package com.appraisehub.controller;

import com.appraisehub.dto.ApiResponse;
import com.appraisehub.dto.NotificationRequestDTO;
import com.appraisehub.dto.NotificationResponseDTO;
import com.appraisehub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @RequestBody NotificationRequestDTO requestDTO) {
        NotificationResponseDTO saved =
                notificationService.createNotification(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification created successfully", saved));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getNotificationsByUserId(
            @PathVariable Long userId) {
        List<NotificationResponseDTO> notifications =
                notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getUnreadNotifications(
            @PathVariable Long userId) {
        List<NotificationResponseDTO> notifications =
                notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> markAsRead(
            @PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(
                ApiResponse.success("Notification marked as read", notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(
                ApiResponse.success("Notification deleted successfully", null));
    }
}