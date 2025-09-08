package com.fastcode.notification.service.strategy;

import com.fastcode.notification.NotificationPropertiesConfiguration;
import com.fastcode.notification.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component("SSE")
public class SseNotificationStrategy implements NotificationStrategy {

    private final Map<String, List<SseEmitter>> recipientEmitters = new ConcurrentHashMap<>();

    @Autowired private NotificationPropertiesConfiguration env;

    public SseEmitter createEmitter(String recipient) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        if (recipient == null) {
            // Add to a general list if no specific recipient is provided
            recipientEmitters.computeIfAbsent(env.getNotificationDefaultRecipientName(), k -> new ArrayList<>()).add(emitter);
        } else {
            // Add to a specific recipient's list
            recipientEmitters.computeIfAbsent(recipient, k -> new ArrayList<>()).add(emitter);
        }

        // Remove emitter on completion, timeout, or error
        emitter.onCompletion(() -> removeEmitter(recipient, emitter));
        emitter.onTimeout(() -> removeEmitter(recipient, emitter));
        emitter.onError(e -> removeEmitter(recipient, emitter));

        return emitter;
    }

    private void removeEmitter(String recipient, SseEmitter emitter) {
        String key = recipient == null ? env.getNotificationDefaultRecipientName() : recipient;
        List<SseEmitter> emitters = recipientEmitters.get(key);

        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                recipientEmitters.remove(key);
            }
        }
    }

    @Override
    public void send(Notification notification) {
        String recipient = notification.getRecipient();
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // Determine the target emitters
        if (recipient == null) {
            // Send to all recipients
            recipientEmitters.forEach((key, emitters) ->
                    sendToEmitters(emitters, notification, deadEmitters)
            );
        } else {
            // Send to specific recipient
            List<SseEmitter> emitters = recipientEmitters.getOrDefault(recipient, Collections.emptyList());
            sendToEmitters(emitters, notification, deadEmitters);
        }

        // Clean up dead emitters
        cleanUpDeadEmitters(deadEmitters);
    }

    private void sendToEmitters(List<SseEmitter> emitters, Notification notification, List<SseEmitter> deadEmitters) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .data(notification));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
    }

    private void cleanUpDeadEmitters(List<SseEmitter> deadEmitters) {
        deadEmitters.forEach(emitter -> recipientEmitters.values().forEach(list -> list.remove(emitter)));
    }

    @Override
    public String getType() {
        return "SSE";
    }
}
