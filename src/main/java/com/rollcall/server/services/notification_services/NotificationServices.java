package com.rollcall.server.services.notification_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Notification;

public interface NotificationServices {
    List<Notification> getNotifications(UUID userId);
}
