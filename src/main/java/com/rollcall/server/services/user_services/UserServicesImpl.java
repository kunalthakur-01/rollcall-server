package com.rollcall.server.services.user_services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.dto.CoordinatorDto;
import com.rollcall.server.dto.UserDto;
import com.rollcall.server.exceptions.ResourceNotFoundException;
// import com.rollcall.server.helper.DaoToDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceAlreadyExistException;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.User;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendeeDao attendeeDao;

    @Autowired
    private CoordinatorDao coordinatorDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ResponseEntity<UserDto> signup(UserDto userDto, Attendee attendee, Coordinator coordinator) {
        User user = dtoToUser(userDto);
        User existingUser = null;

        try {
            existingUser = userDao.findByEmailOrUserNameOrPhone(userDto.getEmail(), userDto.getUserName(), userDto.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException(e.getMessage());
        }

        if(existingUser != null){
            throw new ResourceAlreadyExistException("User already exist with this resource!!");
        }

        if(attendee != null) {
            try { 
                existingUser = userDao.save(user);
                attendee.setUser(existingUser);
                attendeeDao.save(attendee);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerException(e.getMessage());
            }
        }

        if(coordinator != null) {
            try { 
                existingUser = userDao.save(user);
                coordinator.setUser(existingUser);
                coordinatorDao.save(coordinator);
                // System.out.println(newcoordinator);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerException(e.getMessage());
            }
        }

        // try { 
        //     existingUser = userDao.save(user);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     throw new InternalServerException(e.getMessage());
        //     // return new ResponseEntity<>("Something went wrong!!", HttpStatus.INTERNAL_SERVER_ERROR);
        //     // return new ResponseEntity<>(new ResourceNotFoundException("user", "Id", user.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
        // }
        // return new ResponseEntity<>(userToDto(existingUser), HttpStatus.CREATED);
        return ResponseEntity.status(201).body(userToDto(existingUser));
    }

    @Override
    public ResponseEntity<User> login(String email, String password) {
        User user = null;
        try {
            user = userDao.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("Signup failed!!");
        }

        if(user == null) {
            throw new ResourceNotFoundException("user", "email", email);
        }

        if(!user.getPassword().equals(password)) {
            throw new CustomException("Incorrect password", 400);
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public List<Attendee> getAllAttendees() {
        return attendeeDao.findAll();
    }

    @Override
    public List<CoordinatorDto> getAllCoordinators() {
        return coordinatorDao.findAll().stream().map(c -> modelMapper.map(c, CoordinatorDto.class)).collect(Collectors.toList());
    }

    public UserDto userToDto(User user) {
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }
}

