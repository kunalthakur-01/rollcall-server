package com.rollcall.server.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Lecture;

import lombok.Data;

@Data
public class AttendanceDto {
    private UUID id;

    private Lecture lecture;

    private Date attendanceDate = new Date();

    private List<Attendee> presentAttendees;
}
