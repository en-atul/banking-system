package com.proj.notificationservice.repository;

import com.proj.notificationservice.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findByUserIdOrderBySentAtDesc(Long userId);
    
    List<Notification> findByUserIdAndReadOrderBySentAtDesc(Long userId, boolean read);
    
    long countByUserIdAndRead(Long userId, boolean read);
} 