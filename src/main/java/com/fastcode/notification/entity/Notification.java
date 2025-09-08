package com.fastcode.notification.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String message;
    private String type; // EMAIL, SMS, SSE, etc.
    private String status; // SENT, PENDING, FAILED
    private LocalDateTime timestamp;

    @Convert(converter = JsonNodeConverter.class)
    @Column(name = "data", columnDefinition = "jsonb", nullable = true)
    @ColumnTransformer(write = "?::jsonb")
    private JsonNode data; // Flexible JSON data field

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public JsonNode getData() {
        return data;
    }
    public void setData(JsonNode data) {
        this.data = data;
    }
}

