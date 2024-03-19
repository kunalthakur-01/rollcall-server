package com.rollcall.server.dao;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.Coordinator;

public interface CoordinatorDao extends CrudRepository<Coordinator, Integer> {
    
}
