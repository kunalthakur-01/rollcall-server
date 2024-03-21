package com.rollcall.server.models;

import java.util.UUID;

import jakarta.persistence.Column;

// import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "users")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String userName;

    private String name;

    @Column(unique=true)
    private long phone;

    @Column(unique=true)
    private String email;

    private String password;

    private String profession;

    private String dob;
}
