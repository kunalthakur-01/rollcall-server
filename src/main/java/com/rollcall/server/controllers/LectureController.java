package com.rollcall.server.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.dto.LectureDto;
import com.rollcall.server.services.lecture_services.LectureServices;

@RestController
@RequestMapping("/api/user/lecture")
public class LectureController {
    @Autowired
    private LectureServices lectureServices;

    @PostMapping("/new/{groupId}/{coordinatorId}")
    public ResponseEntity<LectureDto> createLecture(@RequestBody LectureDto lectureDto, @PathVariable("groupId") UUID groupId,
            @PathVariable("coordinatorId") UUID coordinatorId) {
        return ResponseEntity.status(201).body(lectureServices.createLecture(lectureDto, groupId, coordinatorId));
        // return null;
    }
}
