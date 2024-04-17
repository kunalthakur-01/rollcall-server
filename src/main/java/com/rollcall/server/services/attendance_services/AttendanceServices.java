package com.rollcall.server.services.attendance_services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.rollcall.server.dto.AttendanceDto;

public interface AttendanceServices {
    public AttendanceDto markAttendance(UUID lectureId, Date date, List<UUID> attendanceList);
    public AttendanceDto getAttendance(UUID lectureId, String date);
}
