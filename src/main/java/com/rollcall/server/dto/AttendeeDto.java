package com.rollcall.server.dto;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Group;
import com.rollcall.server.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AttendeeDto {
    private UUID id;

    private User user;

    @NotEmpty(message = "Roll no cannot be null")
    private String rollNo;

    @NotEmpty(message = "Barnch cannot be null")
    private String branch;

    @NotEmpty(message = "Degree cannot be null")
    private String degree;

    @NotEmpty(message = "College name cannot be null")
    private String collegeName;

    private List<Group> otherGroups;
}
