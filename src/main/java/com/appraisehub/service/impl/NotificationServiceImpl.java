package com.appraisehub.service.impl;

import com.appraisehub.dto.NotificationRequestDTO;
import com.appraisehub.dto.NotificationResponseDTO;
import com.appraisehub.entity.Notification;
import com.appraisehub.entity.User;
import com.appraisehub.exception.ResourceNotFoundException;
import com.appraisehub.mappers.NotificationMapper;
import com.appraisehub.repository.NotificationRepository;
import com.appraisehub.repository.UserRepository;
import com.appraisehub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", requestDTO.getUserId()));

        Notification notification = Notification.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .message(requestDTO.getMessage())
                .type(requestDTO.getType())
                .build();

        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toResponse(saved);
    }

    @Override
    public void send(Long userId, String title, String message,
                     Notification.Type type, Object payload) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_Id(userId)
                .stream()
                .map(NotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_IdAndIsRead(userId, false)
                .stream()
                .map(NotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification", id));
        notification.setRead(true);
        Notification updated = notificationRepository.save(notification);
        return NotificationMapper.toResponse(updated);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification", id));
        notificationRepository.deleteById(id);
    }
} 