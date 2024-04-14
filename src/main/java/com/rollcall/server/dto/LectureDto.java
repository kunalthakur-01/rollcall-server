package com.rollcall.server.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Group;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LectureDto {
    private UUID id;
    
    private Coordinator coordinator;

    private Group group;

    @NotEmpty(message = "Lecture name is required")
    private String lectureName;

    @NotEmpty(message = "Description cannot be null")
    private String description;

    // @NotEmpty(message = "Enter the date on which the lecture created")
    private Date createdOnDate = new Date();

    private int count;

    @NotEmpty(message = "Starting time cannot be null")
    private String startTime;

    @NotEmpty(message = "Ending time cannot be null")
    private String endTime;

    private List<String> schedules;
}
