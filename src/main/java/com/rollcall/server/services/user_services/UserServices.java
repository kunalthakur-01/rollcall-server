package com.rollcall.server.services.user_services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rollcall.server.dto.CoordinatorDto;
import com.rollcall.server.dto.UserDto;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.User;

public interface UserServices {
    public ResponseEntity<UserDto> signup(UserDto userDto, Attendee attendee, Coordinator coordinator);
    public ResponseEntity<User> login(String email, String password);
    public List<Attendee> getAllAttendees();
    public List<CoordinatorDto> getAllCoordinators();
}
