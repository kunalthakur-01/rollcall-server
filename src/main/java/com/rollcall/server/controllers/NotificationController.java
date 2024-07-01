package com.rollcall.server.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.services.notification_services.NotificationServices;

@RestController
@RequestMapping("api/user/notification")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private NotificationServices notificationServices;

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok().body(notificationServices.getNotifications(userId));
    }
}
