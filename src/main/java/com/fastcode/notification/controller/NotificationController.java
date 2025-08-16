package com.fastcode.notification.controller;

import com.fastcode.notification.entity.Notification;
import com.fastcode.notification.service.NotificationService;
import com.fastcode.notification.dto.NotificationDto;
import com.fastcode.notification.service.strategy.SseNotificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SseNotificationStrategy sseNotificationStrategy;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDto notificationDto) {
        Notification notification = notificationService.createNotification(
                notificationDto.getRecipient(),
                notificationDto.getMessage(),
                notificationDto.getType()
        );
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/stream")
    public SseEmitter streamNotifications() {
        return sseNotificationStrategy.createEmitter();
    }
}

