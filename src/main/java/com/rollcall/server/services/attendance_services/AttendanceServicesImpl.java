package com.rollcall.server.services.attendance_services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollcall.server.dao.AttendanceDao;
import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.LectureDao;
import com.rollcall.server.dto.AttendanceDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceNotFoundException;
import com.rollcall.server.models.Attendance;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Lecture;

@Service
public class AttendanceServicesImpl implements AttendanceServices {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private LectureDao lectureDao;

    @Autowired
    private AttendeeDao attendeeDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AttendanceDto markAttendance(UUID lectureId, Date onDate, List<UUID> attendanceList) {
        Lecture existingLecture = null;
        List<Attendance> existingAttendances = new ArrayList<>();

        try {
            existingLecture = lectureDao.findById(lectureId).orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));

            existingAttendances = attendanceDao.findByLecture(existingLecture);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        // // String dateString = date.split("T")[0];

        // // int year = Integer.parseInt(dateString.split("-")[0]);
        // // int month = Integer.parseInt(dateString.split("-")[1]);
        // // int date = Integer.parseInt(dateString.split("-")[2]);

        // // DateTimeFormatter dtf = DateTimeFormatter.RFC_1123_DATE_TIME;
        // // LocalDateTime ldt = LocalDateTime.parse(onDate, dtf);

        // // System.out.println(ldt.getHour());

        Calendar clientCalendar = Calendar.getInstance();
        clientCalendar.setTime(onDate);

        Calendar serverCalendar = Calendar.getInstance();

        for (Attendance attendance : existingAttendances) {
            serverCalendar.setTime(attendance.getAttendanceDate());

            if(clientCalendar.get(Calendar.DATE) != serverCalendar.get(Calendar.DATE)) continue;
            if(clientCalendar.get(Calendar.MONTH) != serverCalendar.get(Calendar.MONTH)) continue;
            if(clientCalendar.get(Calendar.YEAR) != serverCalendar.get(Calendar.YEAR)) continue;
            else {
                throw new CustomException("Attendance already exists", 422);
            }
        }

        List<String> errors = new ArrayList<>();
        Attendee existingAttendee = null;

        Attendance newAttendance = new Attendance();
        newAttendance.setAttendanceDate(onDate);
        newAttendance.setLecture(existingLecture);

        try {
            for (UUID attendeeId: attendanceList) {
                existingAttendee = attendeeDao.findById(attendeeId).orElse(null);
                if(existingAttendee == null) {
                    errors.add(new ResourceNotFoundException("Attendee", "Id", attendeeId.toString()).getMessage());
                    continue;
                }
                // newAttendance.getPresentAttendees().add(existingAttendee);
                existingAttendee.getAttendances().add(newAttendance);
            }

            // newAttendance = attendanceDao.save(newAttendance);
            attendeeDao.save(existingAttendee);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        // if(!errors.isEmpty()) {
        //     throw new MultipleException(errors);
        // }
        
        return modelMapper.map(newAttendance, AttendanceDto.class);
    }

    @Override
    public AttendanceDto getAttendance(UUID lectureId, String onDate) {
        Lecture existingLecture = null;
        List<Attendance> existingAttendances = new ArrayList<>();

        try {
            existingLecture = lectureDao.findById(lectureId).orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));

            existingAttendances = attendanceDao.findByLecture(existingLecture);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        String dateString = onDate.split("T")[0];

        int year = Integer.parseInt(dateString.split("-")[0]);
        int month = Integer.parseInt(dateString.split("-")[1]);
        int date = Integer.parseInt(dateString.split("-")[2]);

        Calendar serverCalendar = Calendar.getInstance();

        for (Attendance attendance : existingAttendances) {
            serverCalendar.setTime(attendance.getAttendanceDate());

            if(date != serverCalendar.get(Calendar.DATE)) continue;
            if(month != serverCalendar.get(Calendar.MONTH) + 1) continue;
            if(year != serverCalendar.get(Calendar.YEAR)) continue;
            else {
                return modelMapper.map(attendance, AttendanceDto.class);
            }
        }
        throw new CustomException("Attendance doesn't exist", 400);
    }
    
}
