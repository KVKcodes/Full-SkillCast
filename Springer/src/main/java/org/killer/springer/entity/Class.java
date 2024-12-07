package org.killer.springer.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private LocalDateTime scheduled_at;
    private LocalTime duration;
    private LocalDateTime started_at;
    private LocalDateTime ended_at;
    private Integer teachers;
    private Integer capacity;
    private Integer subject;
    private String topic;
    private String title;
    private String description;
    private Integer registrations;
    private Integer students;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Add remaining getters and setters
} 