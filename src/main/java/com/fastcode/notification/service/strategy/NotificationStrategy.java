package com.fastcode.notification.service.strategy;

import com.fastcode.notification.entity.Notification;

public interface NotificationStrategy {
    void send(Notification notification);
    String getType(); // Returns the type of notification this strategy handles
}

