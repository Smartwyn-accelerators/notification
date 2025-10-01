package com.fastcode.notification.service;

import com.fastcode.notification.entity.Notification;
import com.fastcode.notification.entity.QNotification;
import com.fastcode.notification.repository.NotificationRepository;
import com.fastcode.notification.service.strategy.NotificationStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private Map<String, NotificationStrategy> notificationStrategies;

    public Notification createNotification(String recipient, String message, String type, JsonNode data) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus("PENDING");
        notification.setData(data);
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
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public Page<Notification> getAllNotifications(String type, String status, String recipient, Pageable pageable) {
        QNotification notification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (type != null) {
            builder.and(notification.type.eq(type));
        }
        if (status != null) {
            builder.and(notification.status.eq(status));
        }
        if (recipient != null) {
            builder.and(notification.recipient.eq(recipient));
        }

        return notificationRepository.findAll(builder, pageable);
    }
}

