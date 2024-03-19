package com.rollcall.server.models;

// import java.util.List;

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
@Table(name = "groups")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @NotEmpty(message = "Group name cannot be null")
    private String groupName;

    @NotEmpty(message = "Degree cannot be null")
    private String degree;

    @NotEmpty(message = "Batch cannot be null")
    private String batch;

    @NotEmpty(message = "Description cannot be null")
    private String description;

    @NotEmpty(message = "IconThemeColor cannot be null")
    private String iconThemeColor;

    // private List<User> attendees;

    // private List<User> coordinators;

    // private List<> lectures;
}
