package com.fastcode.notification.service.strategy;

import com.fastcode.notification.entity.Notification;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("SMS")
public class SmsNotificationStrategy implements NotificationStrategy {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Override
    public void send(Notification notification) {
        Message.creator(
                new PhoneNumber(notification.getRecipient()),
                new PhoneNumber(twilioPhoneNumber),
                notification.getMessage()
        ).create();
    }

    @Override
    public String getType() {
        return "SMS";
    }
}

