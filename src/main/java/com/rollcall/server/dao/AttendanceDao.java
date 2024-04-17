package com.rollcall.server.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Attendance;
import com.rollcall.server.models.Lecture;


public interface AttendanceDao extends JpaRepository<Attendance, UUID> {
    public List<Attendance> findByLecture(Lecture lecture);
}
