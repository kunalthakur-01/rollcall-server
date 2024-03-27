package com.rollcall.server.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dto.UserDto;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.User;
import com.rollcall.server.models.UserAttendee;
import com.rollcall.server.models.UserCoordinator;
import com.rollcall.server.services.user_services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private UserServices userServices;
    
    @PostMapping("/attendee/signup")
    // @GetMapping("signup")
    public ResponseEntity<UserDto> signupAttendee(@Valid @RequestBody UserAttendee userAttendee) {
        // System.out.println(userAttendee);
        return userServices.signup(userAttendee.getUser(), userAttendee.getAttendee(), null);
        // return null;
    }

    @PostMapping("/coordinator/signup")
    // @GetMapping("signup")
    public ResponseEntity<UserDto> signupCoordinator(@Valid @RequestBody UserCoordinator userCoordinator) {
        // System.out.println(userCoordinator);
        return userServices.signup(userCoordinator.getUser(), null, userCoordinator.getCoordinator());
        // return null;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, Object> body){
        String email = (String) body.get("email");
        String password = (String) body.get("password");

        return userServices.login(email, password);
    }

    @GetMapping("/all/attendees")
    public ResponseEntity<List<Attendee>> getAllAttendees(){
        
        return ResponseEntity.status(200).body(userServices.getAllAttendees());
        // return null;
    }

    @GetMapping("/all/coordinators")
    public ResponseEntity<List<Coordinator>> getAllCoordinators(){
        
        return ResponseEntity.status(200).body(userServices.getAllCoordinators());
        // return null;
    }

    @PostMapping("/c")
    public ResponseEntity<Coordinator> createCoordinator(@RequestBody Coordinator coordinator) {
        return ResponseEntity.status(201).body(coordinatorDao.save(coordinator));
    }
}
