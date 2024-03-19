package com.rollcall.server.dao;

import org.springframework.data.repository.CrudRepository;

import com.rollcall.server.models.User;

public interface UserDao extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
    public User findByEmailOrUserNameOrPhone(String email, String userName, long phone);
}
