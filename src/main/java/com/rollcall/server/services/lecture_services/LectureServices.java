package com.rollcall.server.services.lecture_services;

import java.util.UUID;

import com.rollcall.server.dto.LectureDto;

public interface LectureServices {
    public LectureDto createLecture(LectureDto lectureDto, UUID groupId, UUID coordinatorId);
}
