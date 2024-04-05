package com.rollcall.server.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "attendees_classes", joinColumns = {
        @JoinColumn(name ="groupId", referencedColumnName = "id")
    },
    inverseJoinColumns = {
        @JoinColumn(name ="attendeesid", referencedColumnName = "id")
    })
    @JsonIgnore
    private List<Attendee> attendees;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "coordinators_classes", joinColumns = {
        @JoinColumn(name ="groupId", referencedColumnName = "id")
    },
    inverseJoinColumns = {
        @JoinColumn(name ="coordinatorId", referencedColumnName = "id")
    })
    @JsonIgnore
    private List<Coordinator> coordinators;

    @OneToMany(mappedBy = "group")
    private List<Lecture> lectures;
}