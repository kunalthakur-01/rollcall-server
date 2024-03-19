package com.rollcall.server.models;

import com.rollcall.server.dto.UserDto;

import lombok.Data;

@Data
public class UserAttendee {
    private UserDto user;
    private Attendee attendee;
}
