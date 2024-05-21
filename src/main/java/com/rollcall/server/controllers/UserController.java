package com.rollcall.server.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.dto.AttendeeDto;
import com.rollcall.server.dto.CoordinatorDto;
import com.rollcall.server.dto.UserDto;
import com.rollcall.server.models.JwtRequest;
import com.rollcall.server.models.JwtResponse;
import com.rollcall.server.models.RefreshTokenRequest;
import com.rollcall.server.models.User;
import com.rollcall.server.models.UserAttendee;
import com.rollcall.server.models.UserCoordinator;
import com.rollcall.server.services.user_services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/auth/attendee/signup")
    // @GetMapping("signup")
    public ResponseEntity<JwtResponse> signupAttendee(@Valid @RequestBody UserAttendee userAttendee) {
        // System.out.println(userAttendee);
        return userServices.signup(userAttendee.getUser(), userAttendee.getAttendee(), null);
        // return null;
    }

    @PostMapping("/auth/coordinator/signup")
    // @GetMapping("signup")
    public ResponseEntity<JwtResponse> signupCoordinator(@Valid @RequestBody UserCoordinator userCoordinator) {
        System.out.println(userCoordinator);
        return userServices.signup(userCoordinator.getUser(), null, userCoordinator.getCoordinator());
        // return null;
    }

    @PostMapping("/auth/login2")
    public ResponseEntity<User> login(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        return userServices.login(email, password);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login2(@RequestBody JwtRequest jwtRequest) {
        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();

        return ResponseEntity.ok().body(userServices.login2(email, password));                
    }

    @PostMapping("/refresh/jwt")
    public ResponseEntity<JwtResponse> refreshJwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok().body(userServices.refreshjwt(refreshTokenRequest.getRefreshToken()));               
    }

    @GetMapping("/all/attendees")
    public ResponseEntity<List<AttendeeDto>> getAllAttendees() {

        return ResponseEntity.status(200).body(userServices.getAllAttendees());
        // return null;
    }

    @GetMapping("/all/coordinators")
    public ResponseEntity<List<CoordinatorDto>> getAllCoordinators() {

        return ResponseEntity.status(200).body(userServices.getAllCoordinators());
        // return null;
    }

    @PostMapping("/{userId}/{searchBy}")
    public ResponseEntity<List<UserDto>> createCoordinator(@RequestBody List<UUID> alreadyAddedUsers,
            @PathVariable("userId") UUID userId, @PathVariable("searchBy") String searchBy) {
        return ResponseEntity.status(200).body(userServices.getUsersBySearch(alreadyAddedUsers, userId, searchBy));
    }
}
