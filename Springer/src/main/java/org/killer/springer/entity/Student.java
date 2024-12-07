package org.killer.springer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String bio;
    private String college;
    private String pursuing_degree;
    private Integer fav_subjects;

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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPursuing_degree() {
        return pursuing_degree;
    }

    public void setPursuing_degree(String pursuing_degree) {
        this.pursuing_degree = pursuing_degree;
    }

    public Integer getFav_subjects() {
        return fav_subjects;
    }

    public void setFav_subjects(Integer fav_subjects) {
        this.fav_subjects = fav_subjects;
    }
}