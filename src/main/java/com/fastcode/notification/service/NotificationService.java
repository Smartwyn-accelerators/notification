package com.fastcode.notification.service;

import com.fastcode.notification.repository.NotificationRepository;
import com.fastcode.notification.entity.Notification;
import com.fastcode.notification.service.strategy.NotificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private Map<String, NotificationStrategy> notificationStrategies;

    public Notification createNotification(String recipient, String message, String type) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public void sendNotification(Notification notification) {
        NotificationStrategy strategy = notificationStrategies.get(notification.getType().toUpperCase());
        if (strategy != null) {
            strategy.send(notification);
            notification.setStatus("SENT");
        } else {
            notification.setStatus("FAILED");
        }
        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}

