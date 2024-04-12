package com.rollcall.server.dto;

import java.util.List;
import java.util.UUID;

import com.rollcall.server.models.Group;
import com.rollcall.server.models.Lecture;
import com.rollcall.server.models.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CoordinatorDto {
    private UUID id;

    private User user;

    @NotEmpty(message = "Roll cannot be null")
    private String rollNo;

    private List<Group> createdGroups;

    private List<Group> otherGroups;

    private List<Lecture> lectures;
}
