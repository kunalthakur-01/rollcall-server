package com.rollcall.server.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JsonIgnore
    private Coordinator coordinator;

    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonBackReference
    private Group group;

    private String lectureName;

    private String description;

    private Date createdOnDate;

    @Value("${0}")
    private int count;

    private String startTime;

    private String endTime;

    private List<String> schedules;

    @OneToMany(mappedBy = "lecture")
    private List<Attendance> attendances;
}
