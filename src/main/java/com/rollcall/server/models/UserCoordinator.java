package com.rollcall.server.models;

import com.rollcall.server.dto.UserDto;

import lombok.Data;

@Data
public class UserCoordinator {
    private UserDto user;
    private Coordinator coordinator;
}
