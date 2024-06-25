package com.rollcall.server.services.notification_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.StudentNotification;
import com.rollcall.server.models.TeacherNotification;

public interface NotificationServices {
    List<StudentNotification> getStudentNotifications(UUID userId);

    List<TeacherNotification> getTeacherNotifications(UUID userId);
}
