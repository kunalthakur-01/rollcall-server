package com.rollcall.server.models;

import java.util.List;
import java.util.UUID;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attendees")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String rollNo;

    private String branch;

    private String degree;

    private String collegeName;

    @ManyToMany(mappedBy = "attendees")
    @JsonIgnore
    private List<Group> otherGroups;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "attendees_attendance", joinColumns = {
        @JoinColumn(name ="attendeeId", referencedColumnName = "id")
    },
    inverseJoinColumns = {
        @JoinColumn(name ="attendanceId", referencedColumnName = "id")
    })
    @JsonIgnore
    private List<Attendance> attendances;
}
