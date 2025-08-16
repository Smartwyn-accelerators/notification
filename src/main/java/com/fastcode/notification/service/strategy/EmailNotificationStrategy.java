package com.fastcode.notification.service.strategy;

import com.fastcode.notification.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("EMAIL")
public class EmailNotificationStrategy implements NotificationStrategy {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(Notification notification) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(notification.getRecipient());
        mailMessage.setSubject("Notification");
        mailMessage.setText(notification.getMessage());
        javaMailSender.send(mailMessage);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}

