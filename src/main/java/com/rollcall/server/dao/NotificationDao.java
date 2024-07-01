package com.rollcall.server.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Notification;

public interface NotificationDao extends JpaRepository<Notification, UUID> {
    
}
