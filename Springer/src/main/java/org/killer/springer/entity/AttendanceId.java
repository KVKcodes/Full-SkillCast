package org.killer.springer.entity;

import java.io.Serializable;
import java.util.Objects;

public class AttendanceId implements Serializable {
    private Integer studentId;
    private Integer classId;

    public AttendanceId() {
    }

    public AttendanceId(Integer studentId, Integer classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(studentId, that.studentId) && 
               Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, classId);
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
} 