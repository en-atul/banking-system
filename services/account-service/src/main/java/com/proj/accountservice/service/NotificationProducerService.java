package com.proj.accountservice.service;

import com.proj.accountservice.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerService {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Value("${kafka.topic.notifications}")
    private String notificationTopic;

    public void sendNotification(NotificationEvent event) {
        try {
            CompletableFuture<SendResult<String, NotificationEvent>> future = 
                kafkaTemplate.send(notificationTopic, event.getUserId().toString(), event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Notification sent successfully to topic: {}, partition: {}, offset: {}", 
                        notificationTopic, result.getRecordMetadata().partition(), 
                        result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send notification to topic: {}", notificationTopic, ex);
                }
            });
        } catch (Exception e) {
            log.error("Error sending notification to Kafka: {}", e.getMessage(), e);
        }
    }

    public void sendAccountCreatedNotification(Long userId, Long accountId, String accountNumber, 
                                           String accountType, BigDecimal balance) {
        NotificationEvent event = new NotificationEvent();
        event.setEventType("ACCOUNT_CREATED");
        event.setUserId(userId);
        event.setAccountId(accountId);
        event.setAccountNumber(accountNumber);
        event.setAccountType(accountType);
        event.setBalance(balance);
        event.setTimestamp(java.time.LocalDateTime.now());
        event.setMessage("Your account has been created successfully!");
        
        sendNotification(event);
    }

    public void sendAccountUpdatedNotification(Long userId, Long accountId, String accountNumber, 
                                           String accountType, BigDecimal balance) {
        NotificationEvent event = new NotificationEvent();
        event.setEventType("ACCOUNT_UPDATED");
        event.setUserId(userId);
        event.setAccountId(accountId);
        event.setAccountNumber(accountNumber);
        event.setAccountType(accountType);
        event.setBalance(balance);
        event.setTimestamp(java.time.LocalDateTime.now());
        event.setMessage("Your account has been updated successfully!");
        
        sendNotification(event);
    }
} 