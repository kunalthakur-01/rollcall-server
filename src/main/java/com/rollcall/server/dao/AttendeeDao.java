package com.rollcall.server.dao;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.Attendee;

public interface AttendeeDao extends CrudRepository<Attendee, Integer>  {
     
}
