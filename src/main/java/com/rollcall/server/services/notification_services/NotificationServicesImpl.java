package com.rollcall.server.services.notification_services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.StudentNotification;
import com.rollcall.server.models.TeacherNotification;
import com.rollcall.server.models.User;

@Service
public class NotificationServicesImpl implements NotificationServices {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendeeDao attendeeDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Override
    public List<StudentNotification> getStudentNotifications(UUID userId) {
        User existingUser = null;
        Attendee existingAttendee = null;

        try {
            existingUser = userDao.findById(null)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", String.format("%s", userId)));
            existingAttendee = attendeeDao.findByUser(existingUser);

        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingAttendee == null) {
            throw new CustomException("Invalid attendee Id", 400);
        }

        return existingAttendee.getNotifications();
    }

    @Override
    public List<TeacherNotification> getTeacherNotifications(UUID userId) {
        User existingUser = null;
        Coordinator existingCoordinator = null;

        try {
            existingUser = userDao.findById(null)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", String.format("%s", userId)));
            existingCoordinator = coordinatorDao.findByUser(existingUser);

        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        if (existingCoordinator == null) {
            throw new CustomException("Invalid coordinator Id", 400);
        }

        return existingCoordinator.getNotifications();
    }

}
