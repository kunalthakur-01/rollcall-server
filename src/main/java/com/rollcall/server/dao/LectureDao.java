package com.rollcall.server.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Lecture;

public interface LectureDao extends JpaRepository<Lecture, UUID> {
    
}
