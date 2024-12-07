package org.killer.springer.repository;

import java.util.List;

import org.killer.springer.entity.Attendance;
import org.killer.springer.entity.AttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {
    List<Attendance> findByStudentId(Integer studentId);
    List<Attendance> findByClassId(Integer classId);
} 