package org.killer.springer.entity;

import java.time.LocalDateTime;
import java.time.Duration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
@IdClass(AttendanceId.class)
public class Attendance {
    
    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @Id
    @Column(name = "class_id")
    private Integer classId;

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    private Class classObj;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private Duration duration;
    
    private LocalDateTime joinedAt;
    
    private LocalDateTime leftAt;

    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        LATE,
        LEFT_EARLY
    }

    // Getters and Setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Class getClassObj() {
        return classObj;
    }

    public void setClassObj(Class classObj) {
        this.classObj = classObj;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
        if (this.joinedAt != null && leftAt != null) {
            this.duration = Duration.between(this.joinedAt, leftAt);
        }
    }
} 