package com.rollcall.server.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Lecture;
import com.rollcall.server.payloads.ApiResponse;
import com.rollcall.server.services.lecture_services.LectureServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/lecture")
@CrossOrigin("*")
public class LectureController {
    @Autowired
    private LectureServices lectureServices;

    @PostMapping("/new/{groupId}/{userId}")
    public ResponseEntity<ApiResponse> createLecture(@Valid @RequestBody LectureDto lectureDto,
            @PathVariable("groupId") UUID groupId,
            @PathVariable("userId") UUID userId) {
        return ResponseEntity.status(201).body(lectureServices.createLecture(lectureDto, groupId, userId));
        // return null;
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Lecture>> getAllLecturesByUserId(@PathVariable("userId") UUID userId,
            @RequestParam(required = true, name = "onDate") String onDate) {
        return ResponseEntity.ok().body(lectureServices.getLecturesByUserId(userId, onDate));
    }

    @GetMapping("/{lectId}")
    public ResponseEntity<LectureDto> getLectureById(@PathVariable("lectId") UUID lectId) {
        return ResponseEntity.ok().body(lectureServices.getLectureById(lectId));
    }

    @GetMapping("/attendees/{lectureId}")
    public ResponseEntity<List<Attendee>> getAttendeesByGroupId(@PathVariable("lectureId") UUID lectureId){
        return ResponseEntity.status(200).body(lectureServices.getAttendeesBylectureId(lectureId));
    }

    @GetMapping("all")
    public ResponseEntity<List<LectureDto>> allLectures() {
        return ResponseEntity.ok().body(lectureServices.getAllLecture());
    }
}
