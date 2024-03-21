package com.rollcall.server.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.User;

public interface UserDao extends CrudRepository<User, UUID> {
    public User findByEmail(String email);
    public User findByEmailOrUserNameOrPhone(String email, String userName, long phone);
}
