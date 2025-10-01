package com.fastcode.notification.repository;

import com.fastcode.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Long>, QuerydslPredicateExecutor<Notification> {

}

