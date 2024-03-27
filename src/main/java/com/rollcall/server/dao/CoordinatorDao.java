package com.rollcall.server.dao;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rollcall.server.models.Coordinator;
import com.rollcall.server.models.User;

public interface CoordinatorDao extends JpaRepository<Coordinator, UUID> {
    public Coordinator findByUser(User user);
}
