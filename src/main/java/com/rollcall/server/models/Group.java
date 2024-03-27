package com.rollcall.server.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "classes")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "admin")
    @JsonBackReference
    private Coordinator coordinator;

    private String groupName;

    private String degree;

    private String batch;

    private String description;

    private String iconThemeColor;

    // private List<User> attendees;

    // private List<User> coordinators;

    // private List<> lectures;
}