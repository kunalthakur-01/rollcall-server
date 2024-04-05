package com.rollcall.server.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Attendee;
import com.rollcall.server.models.User;

public interface AttendeeDao extends JpaRepository<Attendee, UUID>  {
     public Attendee findByUser(User user);
}
