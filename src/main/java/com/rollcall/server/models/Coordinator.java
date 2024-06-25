package com.rollcall.server.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

// import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coordinators")
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String rollNo;

    @OneToMany(mappedBy = "admin")
    // @JsonManagedReference
    @JsonIgnore
    private List<Group> createdGroups;

    @ManyToMany(mappedBy = "coordinators")
    // @JsonManagedReference
    @JsonIgnore
    private List<Group> otherGroups;

    @OneToMany(mappedBy = "coordinator")
    @JsonIgnore
    private List<Lecture> lectures;

    @ManyToMany(mappedBy = "coordinators")
    private List<TeacherNotification> notifications;
}
