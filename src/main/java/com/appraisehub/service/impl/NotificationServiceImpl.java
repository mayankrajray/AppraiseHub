package com.appraisehub.service.impl;

import com.appraisehub.dto.NotificationRequestDTO;
import com.appraisehub.dto.NotificationResponseDTO;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.model.Notification;
import com.appraisehub.repository.NotificationRepository;
import com.appraisehub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    private NotificationResponseDTO convertToResponseDTO(Notification notification) {
        NotificationResponseDTO responseDTO = new NotificationResponseDTO();
        responseDTO.setId(notification.getId());
        responseDTO.setUserId(notification.getUserId());
        responseDTO.setMessage(notification.getMessage());
        responseDTO.setIsRead(notification.getIsRead());
        responseDTO.setCreatedAt(notification.getCreatedAt());
        return responseDTO;
    }

    private Notification convertToEntity(NotificationRequestDTO requestDTO) {
        Notification notification = new Notification();
        notification.setUserId(requestDTO.getUserId());
        notification.setMessage(requestDTO.getMessage());
        return notification;
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        Notification notification = convertToEntity(requestDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponseDTO(savedNotification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + id));
        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToResponseDTO(updatedNotification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + id));
        notificationRepository.deleteById(id);
    }
}