package com.rollcall.server.services.lecture_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Lecture;
import com.rollcall.server.payloads.ApiResponse;

public interface LectureServices {
    public ApiResponse createLecture(LectureDto lectureDto, UUID groupId, UUID userId);
    public List<LectureDto> getAllLecture();
    public LectureDto getLectureById(UUID lectId);
    public List<Lecture> getLecturesByUserId(UUID userId, String onDate);
    public List<Attendee> getAttendeesBylectureId(UUID lectureId);
}
