package com.rollcall.server.dto;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Group;
import com.rollcall.server.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LectureDto {
    private UUID id;
    
    private User coordinator;

    private Group group;

    @NotEmpty(message = "Lecture name is required")
    private String lectureName;

    @NotEmpty(message = "Description cannot be null")
    private String description;

    @NotEmpty(message = "Enter the date on which the lecture created")
    private String createdOnDate;

    private int count;

    @NotEmpty(message = "Starting time cannot be null")
    private String startTime;

    @NotEmpty(message = "Ending time cannot be null")
    private String endTime;

    private List<String> schedules;
}
