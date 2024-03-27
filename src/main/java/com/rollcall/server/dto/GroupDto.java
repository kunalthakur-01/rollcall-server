package com.rollcall.server.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GroupDto {
    private UUID id;

    private CoordinatorDto coordinator;

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
}
