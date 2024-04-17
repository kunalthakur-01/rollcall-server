package com.rollcall.server.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.dto.AttendanceDto;
import com.rollcall.server.services.attendance_services.AttendanceServices;

@RestController
@RequestMapping("/api/user/attendance")
@CrossOrigin("*")
public class AttendanceController {
    @Autowired
    private AttendanceServices attendanceServices;

    @PostMapping("/new/{lectureId}/{onDate}")
    public ResponseEntity<AttendanceDto> markNewAttendance(@RequestBody List<UUID> attendanceList, @PathVariable("lectureId") UUID lectureId, @PathVariable("onDate") Date onDate) {
        // UUID lectureId = (UUID) body.get("lectId");
        // Date date = (Date) body.get("date");
        // @SuppressWarnings("unchecked")
        // List<UUID> attendanceList = (List<UUID>) body.get("attendanceList");

        return ResponseEntity.status(201).body(attendanceServices.markAttendance(lectureId, onDate, attendanceList));
        // return null;
    }

    @GetMapping("/{lectureId}/{date}")
    public ResponseEntity<AttendanceDto> getAttendanceByLectIdAndDate(@PathVariable("lectureId") UUID lectureId, @PathVariable("date") String date){
        return ResponseEntity.ok().body(attendanceServices.getAttendance(lectureId, date));
        // return null;
    }
}
