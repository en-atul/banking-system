package com.proj.notificationservice.service;

import com.proj.notificationservice.dto.NotificationEvent;
import com.proj.notificationservice.model.Notification;
import com.proj.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "${kafka.topic.notifications}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeNotification(NotificationEvent event) {
        try {
            log.info("Received notification event: {}", event);
            
            // Create notification entity
            Notification notification = new Notification();
            notification.setUserId(event.getUserId());
            notification.setTitle("Account " + event.getEventType().replace("_", " "));
            notification.setMessage(event.getMessage());
            notification.setType(event.getEventType());
            notification.setSentAt(event.getTimestamp());
            
            // Save to MongoDB
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Notification saved successfully with ID: {}", savedNotification.getId());
            
        } catch (Exception e) {
            log.error("Error processing notification event: {}", e.getMessage(), e);
        }
    }
} 