package org.killer.springer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.killer.springer.entity.Attendance;
import org.killer.springer.entity.AttendanceId;
import org.killer.springer.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance markAttendance(Integer studentId, Integer classId) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setClassId(classId);
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendance.setJoinedAt(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    public Attendance updateLeaveTime(Integer studentId, Integer classId) {
        AttendanceId id = new AttendanceId(studentId, classId);
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendance.setLeftAt(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentAttendance(Integer studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getClassAttendance(Integer classId) {
        return attendanceRepository.findByClassId(classId);
    }
} 