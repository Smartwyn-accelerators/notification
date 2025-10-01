package com.fastcode.notification.controller;

import com.fastcode.notification.NotificationPropertiesConfiguration;
import com.fastcode.notification.common.OffsetBasedPageRequest;
import com.fastcode.notification.entity.Notification;
import com.fastcode.notification.service.NotificationService;
import com.fastcode.notification.dto.NotificationDto;
import com.fastcode.notification.service.strategy.SseNotificationStrategy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SseNotificationStrategy sseNotificationStrategy;

    @Autowired
    private NotificationPropertiesConfiguration env;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDto notificationDto) {
        Notification notification = notificationService.createNotification(
                notificationDto.getRecipient(),
                notificationDto.getMessage(),
                notificationDto.getType(),
                notificationDto.getData()
        );
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<Page<Notification>> getAllNotifications(@RequestParam(value="type", required=false) String type,
                                                                  @RequestParam(value="status", required=false) String status,
                                                                  @RequestParam(value="recipient", required=false) String recipient,
//                                                                  @RequestParam(value="jsonField", required=false) String jsonField,
//                                                                  @RequestParam(value="jsonValue", required=false) String jsonValue,
                                                                  @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort, HttpServletRequest request) {
        if (offset == null) { offset = env.getFastCodeOffsetDefault(); }
        if (limit == null) { limit = env.getFastCodeLimitDefault(); }

        Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);

        String token = extractTokenFromHeader(request);
        if (token == null){
            return ResponseEntity.ok(notificationService.getAllNotifications(type,status,recipient,pageable));
        }
        String sub = getSubjectFromToken(token);

        return ResponseEntity.ok(notificationService.getAllNotifications(type,status,sub,pageable));
    }

    @GetMapping("/stream")
    public SseEmitter streamNotifications(
            @RequestParam(value="recipient", required=false) String recipient,
            HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null){
            return sseNotificationStrategy.createEmitter(recipient);
        }
        String sub = getSubjectFromToken(token);
        return sseNotificationStrategy.createEmitter(sub);
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(env.getNotificationAuthHeaderName());
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7);
            } else if (authorizationHeader.startsWith("Bearer_")) {
                return authorizationHeader.substring(7);
            } else {
                return authorizationHeader;
            }
        }
        return null;
    }

    public String getSubjectFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(env.getNotificationJwtSecretKey().getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            System.out.println("Failed to extract subject from token: " + e.getMessage());
            return null;
        }
    }
}

