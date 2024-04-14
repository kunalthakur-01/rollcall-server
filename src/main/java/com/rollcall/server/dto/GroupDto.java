package com.rollcall.server.dto;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.Lecture;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GroupDto {
    private UUID id;

    private Coordinator admin;

    @NotEmpty(message = "Group name cannot be null")
    private String groupName;

    @NotEmpty(message = "Degree cannot be null")
    private String degree;

    @NotEmpty(message = "Batch cannot be null")
    private String batch;

    @NotEmpty(message = "Description cannot be null")
    private String description;

    @NotEmpty(message = "IconThemeColor cannot be null")
    private String iconThemeColor;

    private List<Attendee> attendees;

    private List<Coordinator> coordinators;

    private List<Lecture> lectures;
}
