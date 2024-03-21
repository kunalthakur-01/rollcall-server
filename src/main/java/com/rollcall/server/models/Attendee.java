package com.rollcall.server.models;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "attendees")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @NotEmpty(message = "Roll no cannot be null")
    private String rollNo;

    @NotEmpty(message = "Barnch cannot be null")
    private String branch;

    @NotEmpty(message = "Degree cannot be null")
    private String degree;

    @NotEmpty(message = "College name cannot be null")
    private String collegeName;

    // private List<User> groups;

}
