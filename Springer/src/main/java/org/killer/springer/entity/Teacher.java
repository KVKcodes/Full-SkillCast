package org.killer.springer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String bio;
    private Integer specialised_subjects;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getSpecialised_subjects() {
        return specialised_subjects;
    }

    public void setSpecialised_subjects(Integer specialised_subjects) {
        this.specialised_subjects = specialised_subjects;
    }
}