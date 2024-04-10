package com.rollcall.server.services.lecture_services;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.payloads.ApiResponse;

public interface LectureServices {
    public ApiResponse createLecture(LectureDto lectureDto, UUID groupId, UUID userId);
    public List<LectureDto> getAllLecture();
    public LectureDto getLectureById(UUID lectId);
}
