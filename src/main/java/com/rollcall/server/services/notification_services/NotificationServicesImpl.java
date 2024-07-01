package com.rollcall.server.services.notification_services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.UserDao;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Notification;
import com.rollcall.server.models.User;

@Service
public class NotificationServicesImpl implements NotificationServices {

    @Autowired
    private UserDao userDao;

    @Override
    public List<Notification> getNotifications(UUID userId) {
        User existingUser = null;

        try {
            existingUser = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", String.format("%s", userId)));

        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        return existingUser.getNotifications();
    }
}
