package com.rollcall.server.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.Attendee;

public interface AttendeeDao extends CrudRepository<Attendee, UUID>  {
     public List<Attendee> findAll();
}
