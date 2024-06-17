package com.rollcall.server.services.user_services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rollcall.server.dao.AttendeeDao;
import com.rollcall.server.dao.CoordinatorDao;
import com.rollcall.server.dao.UserDao;
import com.rollcall.server.dto.AttendeeDto;
import com.rollcall.server.dto.CoordinatorDto;
import com.rollcall.server.dto.UserDto;
import com.rollcall.server.exceptions.ResourceNotFoundException;
// import com.rollcall.server.helper.DaoToDto;
import com.rollcall.server.exceptions.CustomException;
import com.rollcall.server.exceptions.InternalServerException;
import com.rollcall.server.exceptions.ResourceAlreadyExistException;
import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.JwtResponse;
import com.rollcall.server.models.RefreshToken;
import com.rollcall.server.models.User;
import com.rollcall.server.security.JwtHelper;
import com.rollcall.server.services.refreshToken_services.RefreshTokenServices;

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

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenServices refreshTokenServices;

    @Override
    @Transactional
    public ResponseEntity<JwtResponse> signup(UserDto userDto, Attendee attendee, Coordinator coordinator) {
        User user = dtoToUser(userDto);
        User existingUser = null;

        try {
            existingUser = userDao.findByEmailOrUserNameOrPhone(userDto.getEmail(), userDto.getUserName(),
                    userDto.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException(e.getMessage());
        }

        if (existingUser != null) {
            throw new ResourceAlreadyExistException("User already exist with this resource!!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (attendee != null) {
            try {
                existingUser = userDao.save(user);
                attendee.setUser(existingUser);
                attendeeDao.save(attendee);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerException(e.getMessage());
            }
        }

        if (coordinator != null) {
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

        String token = this.jwtHelper.generateToken(existingUser);

        RefreshToken refreshToken = refreshTokenServices.createRefreshToken(existingUser.getEmail());

        JwtResponse response = JwtResponse.builder()
                .id(existingUser.getId())
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .email(existingUser.getUsername())
                .userName(existingUser.getUserName())
                .profession(existingUser.getProfession())
                .tokenValidity(24*60*60*1000)
                .build();

        return ResponseEntity.status(201).body(response);

        // return ResponseEntity.status(201).body(userToDto(existingUser));

        // try {
        // existingUser = userDao.save(user);
        // } catch (Exception e) {
        // e.printStackTrace();
        // throw new InternalServerException(e.getMessage());
        // // return new ResponseEntity<>("Something went wrong!!",
        // HttpStatus.INTERNAL_SERVER_ERROR);
        // // return new ResponseEntity<>(new ResourceNotFoundException("user", "Id",
        // user.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
        // }
        // return new ResponseEntity<>(userToDto(existingUser), HttpStatus.CREATED);
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

        if (user == null) {
            throw new ResourceNotFoundException("user", "email", email);
        }

        if (!user.getPassword().equals(password)) {
            throw new CustomException("Incorrect password", 400);
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public JwtResponse login2(String email, String password) {
        this.doAuthenticate(email, password);

        User userDetails = (User) userDetailsService.loadUserByUsername(email);

        String token = this.jwtHelper.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenServices.createRefreshToken(userDetails.getUsername());

        JwtResponse response = JwtResponse.builder()
                .id(userDetails.getId())
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .email(userDetails.getUsername())
                .userName(userDetails.getUserName())
                .profession(userDetails.getProfession())
                .tokenValidity(24*60*60*1000)
                .build();

        return response;
    }

    @Override
    public JwtResponse refreshjwt(String refreshToken) {
        RefreshToken refreshToken2 = refreshTokenServices.verifyRefreshToken(refreshToken);

        User user = refreshToken2.getUser();

        String token = this.jwtHelper.generateToken(user);

        RefreshToken newRefreshToken = refreshTokenServices.createRefreshToken(user.getUsername());

        JwtResponse response = JwtResponse.builder()
                .id(user.getId())
                .jwtToken(token)
                .refreshToken(newRefreshToken.getRefreshToken())
                .email(user.getUsername())
                .userName(user.getUserName())
                .profession(user.getProfession())
                .tokenValidity(24*60*60*1000)
                .build();

        return response;
    }

    @Override
    public List<UserDto> getUsersBySearch(List<UUID> alreadyAddedUsers, UUID userId, String searchBy) {
        List<User> matchedusers = new ArrayList<>();
        List<User> filteredUsers = new ArrayList<>();

        try {
            matchedusers = userDao.findByUserNameContainingOrNameContaining(searchBy, searchBy);
            // User user = userDao.findById(userId).orElseThrow(() -> new
            // ResourceNotFoundException("User", "Id", userId.toString()));

            for (User user : matchedusers) {
                if (!alreadyAddedUsers.contains(user.getId())) {
                    filteredUsers.add(user);
                }
            }
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

        List<User> removedAdmin = filteredUsers.stream().filter(user -> !user.getId().equals(userId))
                .collect(Collectors.toList());
        return removedAdmin.stream().map(user -> userToDto(user)).collect(Collectors.toList());
    }

    @Override
    public List<AttendeeDto> getAllAttendees() {
        return attendeeDao.findAll().stream().map(a -> modelMapper.map(a, AttendeeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CoordinatorDto> getAllCoordinators() {
        return coordinatorDao.findAll().stream().map(c -> modelMapper.map(c, CoordinatorDto.class))
                .collect(Collectors.toList());
    }

    public UserDto userToDto(User user) {
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credentials Invalid !!");
        }
    }
}
