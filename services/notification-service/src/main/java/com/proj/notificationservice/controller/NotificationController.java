package com.proj.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @PostMapping
    public void createNotification(){
    }

    @GetMapping("/user/{userId}")
    public void getAllNotifications(@PathVariable Long userId){
    }

    @GetMapping("/{notificationId}")
    public void getNotification(@PathVariable Long notificationId){
    }

    @PutMapping("/{notificationId}/read")
    public void markNotificationAsRead(@PathVariable Long notificationId){
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId){
    }
}
